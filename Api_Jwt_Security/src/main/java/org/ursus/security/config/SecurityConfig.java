package org.ursus.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // created JWT filter is not yet used. We will do it in this configuration class
    // There is always a "white list of URL" within an application. That means some endpoint do not require any authentication
    // or tokens for example creating an account does not need any JWT token. Also the same for the login.

    private final JwtAuthFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthFilter jwtFilter, AuthenticationProvider authenticationProvider) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                // whitelisting all requests matching provided below pattern,
                // they will not need any authentication to be executed
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                        "").permitAll()
                        .anyRequest()   // but any other requests
                        .authenticated()) // should be authenticated
                // user session should be stateless, that means we don't want to store the information about the session/
                // Policy of the application is to ensure that every HTTP request is authenticated.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // it is a DTO responsible for fetching UserDetails, encoding passwords etc...
                .authenticationProvider(authenticationProvider)
                // the filter will be executed BEFORE the filter called UsernamePasswordAuthenticationFilter - managed by Spring
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
