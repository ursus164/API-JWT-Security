package org.ursus.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {
    // filter will be executed for the every HTTP request
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,     // HTTP request
            @NonNull HttpServletResponse response,   // HTTP response
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {
        // check if JWT is provided
        final String authHeader = request.getHeader("Authorization");   // if the JWT is provided, it should be in the request header

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // the JWT is only valid when it starts with "Bearer "
            filterChain.doFilter(request,response);
            return;
        }
        // token exists, extracting it from the Authorization header.
        final String jwt = authHeader.substring(7);
        // todo extract the userEmail from JWT token
        final String username = jwtService.extractUsername(jwt); // email is used as a username in the app

    }
}
