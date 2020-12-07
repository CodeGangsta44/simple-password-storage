package com.yurwar.simplepasswordstorage.controller;

import com.yurwar.simplepasswordstorage.controller.dto.UserRegistrationDto;
import com.yurwar.simplepasswordstorage.model.service.UserService;
import com.yurwar.simplepasswordstorage.utils.NotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Boolean> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        boolean isCreated = userService.createUser(userRegistrationDto);
        if (isCreated) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(false,HttpStatus.CONFLICT);
        }
    }
}
