package org.ursus.security.user;

public class UserBuilder {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;

    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public UserBuilder setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(this.id);
        user.setFirstname(this.firstname);
        user.setLastname(this.lastname);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }
}