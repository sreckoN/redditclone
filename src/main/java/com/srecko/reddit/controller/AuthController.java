package com.srecko.reddit.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.AuthorizationHeaderMissingException;
import com.srecko.reddit.exception.RegistrationRequestException;
import com.srecko.reddit.jwt.JwtConfig;
import com.srecko.reddit.jwt.JwtUtil;
import com.srecko.reddit.service.AuthenticationService;
import com.srecko.reddit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, JwtConfig jwtConfig, UserService userService) {
        this.authenticationService = authenticationService;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new RegistrationRequestException(bindingResult.getAllErrors());
        authenticationService.saveUser(registrationRequest);
        return ResponseEntity.ok("User has been registered successfully.");
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                JwtUtil jwtUtil = new JwtUtil(jwtConfig);
                org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", jwtUtil.getAccessToken(user1));
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new AuthorizationHeaderMissingException("Authorization header is missing.");
        }
    }
}