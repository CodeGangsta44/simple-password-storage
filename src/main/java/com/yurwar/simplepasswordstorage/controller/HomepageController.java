package com.yurwar.simplepasswordstorage.controller;

import com.yurwar.simplepasswordstorage.controller.dto.UserDto;
import com.yurwar.simplepasswordstorage.model.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomepageController {

    private final UserService userService;

    public HomepageController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public UserDto getNameOfCurrentUser() {

        return UserDto.builder().username(userService.getCurrentUser().getUsername()).build();
    }
}
