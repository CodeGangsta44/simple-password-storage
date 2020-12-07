package com.yurwar.simplepasswordstorage.controller;

import com.yurwar.simplepasswordstorage.controller.dto.UserLoginDto;
import com.yurwar.simplepasswordstorage.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Boolean> loginUser(@RequestBody UserLoginDto userLoginDto) {
        userService.loadUserByUsername(userLoginDto.getUsername());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
