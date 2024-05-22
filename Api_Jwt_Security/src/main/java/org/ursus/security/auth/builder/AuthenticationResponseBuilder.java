package org.ursus.security.auth.builder;

import org.ursus.security.auth.AuthenticationResponse;

public class AuthenticationResponseBuilder {
    private String jwt;

    public AuthenticationResponseBuilder setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public AuthenticationResponse build() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setJwt(jwt);

        return authenticationResponse;
    }
}
