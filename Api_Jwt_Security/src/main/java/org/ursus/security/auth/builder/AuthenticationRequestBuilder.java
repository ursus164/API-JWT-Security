package org.ursus.security.auth.builder;

public class AuthenticationRequestBuilder {
    private String email;
    private String password;

    public AuthenticationRequestBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public AuthenticationRequestBuilder setPassword(String password) {
        this.password = password;
        return this;
    }
}
