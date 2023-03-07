package com.srecko.reddit.controller;

import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.dto.TokenRefreshRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.exception.RegistrationRequestException;
import com.srecko.reddit.jwt.JwtConfig;
import com.srecko.reddit.service.AuthenticationService;
import com.srecko.reddit.service.RefreshTokenService;
import com.srecko.reddit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtConfig jwtConfig;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, JwtConfig jwtConfig, UserService userService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(HttpServletRequest request, @Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new RegistrationRequestException(bindingResult.getAllErrors());
        String confirmationUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/auth/registrationConfirm?token=";
        authenticationService.register(registrationRequest, confirmationUrl);
        return ResponseEntity.ok("User has been registered successfully. Verify your email to enable the account.");
    }

    @GetMapping("registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        EmailVerificationToken verificationToken = authenticationService.getVerificationToken(token);
        authenticationService.enableUserAccount(verificationToken.getUser());
        return ResponseEntity.ok("Email confirmed! User account activated.");
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new RegistrationRequestException(bindingResult.getAllErrors());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new RegistrationRequestException(bindingResult.getAllErrors());
        AuthenticationResponse newAccessToken = refreshTokenService.getNewAccessToken(tokenRefreshRequest);
        return ResponseEntity.ok(newAccessToken);
    }

    @GetMapping("currentUser")
    public ResponseEntity<String> getCurrentlyLoggedInUser() {
        return ResponseEntity.ok(authenticationService.getCurrentlyLoggedInUser());
    }
}