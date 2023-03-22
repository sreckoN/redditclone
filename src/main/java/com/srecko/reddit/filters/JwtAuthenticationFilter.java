package com.srecko.reddit.filters;

import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath.equals("/api/auth/register") || servletPath.equals("/api/auth/registrationConfirm")
                || servletPath.equals("/api/auth/authenticate") || servletPath.contains("/api/auth/token/refresh")
                || servletPath.contains("/api/search")) {
            filterChain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader("AUTHORIZATION");
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
            }
            String jwtToken = authHeader.substring(7);
            String username = jwtUtils.extractSubject(jwtToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userInstance = userService.getUserByUsername(username);
                UserMediator user = new UserMediator(userInstance);
                if (jwtUtils.isTokenValid(jwtToken, user.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
