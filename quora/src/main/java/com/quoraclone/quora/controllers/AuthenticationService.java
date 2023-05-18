package com.quoraclone.quora.controllers;

import com.quoraclone.quora.config.JwtService;
import com.quoraclone.quora.dao.UserRepository;
import com.quoraclone.quora.entity.Role;
import lombok.RequiredArgsConstructor;
import com.quoraclone.quora.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(new HashMap<String, Object>(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getName(),
                request.getPassword()
        ));
        var user = repository.findByNameEquals(request.getName());

        var jwtToken = jwtService.generateToken(new HashMap<String, Object>(), user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
