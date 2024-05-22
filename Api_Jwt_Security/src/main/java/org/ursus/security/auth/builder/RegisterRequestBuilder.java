package org.ursus.security.auth.builder;

public class RegisterRequestBuilder {
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public RegisterRequestBuilder setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public RegisterRequestBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterRequestBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterRequestBuilder setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
}
