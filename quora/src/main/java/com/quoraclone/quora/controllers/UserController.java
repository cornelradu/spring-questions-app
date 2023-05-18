package com.quoraclone.quora.controllers;

import com.quoraclone.quora.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.quoraclone.quora.services.UserService;
@RestController

class UserController {

    private final UserService service;

    @Autowired
    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    List<User> all() {
        return this.service.getUsers();
    }
}
