package com.srecko.reddit.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.jwt.JwtConfig;
import com.srecko.reddit.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtConfig jwtConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserMediator userMediator = (UserMediator) authentication.getPrincipal();
        JwtUtils jwtUtils = new JwtUtils(jwtConfig);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtUtils.getAccessToken(userMediator.getUsername()));
        tokens.put("refresh_token", jwtUtils.getRefreshToken(userMediator.getUsername()));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
