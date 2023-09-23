package com.quoraclone.quora.controllers;

import com.quoraclone.quora.dtos.UserDto;
import com.quoraclone.quora.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.quoraclone.quora.services.UserService;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
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

    @CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
    @GetMapping("/user")
    ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        return ResponseEntity.ok(new UserDto(service.getUser(authorizationHeader.substring(7))));
    }
}
