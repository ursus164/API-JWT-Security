package org.ursus.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {
    // filter will be executed for the every HTTP request
    private final JwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtServiceImpl jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,     // HTTP request
            @NonNull HttpServletResponse response,   // HTTP response
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {
        // check if JWT is provided
        final String authHeader = request.getHeader("Authorization");

        // check if the JWT is provided, it should be in the request header, or else return
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // the JWT is only valid when it starts with "Bearer "
            filterChain.doFilter(request,response);
            return;
        }
        // token exists, extracting it from the Authorization header.
        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt); // email is used as a username in the app

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // extracting username (user email) from the JWT is possible, and also checking if user is authenticated or not.
            // The information about whether the user is authenticated is stored in the SecurityContextHolder.
            // If the getAuthentication() return null, that means the user is not yet authenticated / connected.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtService.isTokenValid(jwt, userDetails)) {
                // token is valid
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ); // needed to update security context holder
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)  // building details by provided data from the request
                        // like session ID, and IP address - it is needed for the authentication token
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request,response);

    }
}
