package org.ursus.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ursus.security.auth.builder.AuthenticationResponseBuilder;
import org.ursus.security.config.JwtServiceImpl;
import org.ursus.security.user.Role;
import org.ursus.security.user.UserBuilder;
import org.ursus.security.user.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        // create user, save it to a database, and return a generated JWT
        var user = new UserBuilder()
                .setFirstname(request.getFirstname())
                .setLastname(request.getLastname())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()))// password needs to be encoded before saving it to DB
                .setRole(Role.USER)
                .build();
        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponseBuilder()
                .setJwt(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // AuthenticationManager Beans has method called Authenticate which allow us to authenticate a user based
        // on username and password.
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
                )
        );
        // user is authenticated - credentials are correct
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwt = jwtService.generateToken(user);
        return new AuthenticationResponseBuilder()
                .setJwt(jwt)
                .build();
    }
}
