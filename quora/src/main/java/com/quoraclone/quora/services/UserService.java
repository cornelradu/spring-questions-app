package com.quoraclone.quora.services;

import com.quoraclone.quora.config.JwtService;
import com.quoraclone.quora.entity.User;
import com.quoraclone.quora.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Service
public class UserService {

    private final UserRepository repository;

    private final JwtService jwtService;

    @Autowired
    UserService(UserRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public List<User> getUsers() {
        return repository.findAll();
    }

    public String getUser(String token){
        String username = jwtService.extractUsername(token);
        return username;
    }
}
