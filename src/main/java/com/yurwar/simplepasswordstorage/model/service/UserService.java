package com.yurwar.simplepasswordstorage.model.service;

import com.yurwar.simplepasswordstorage.controller.dto.UserLoginDto;
import com.yurwar.simplepasswordstorage.controller.dto.UserRegistrationDto;
import com.yurwar.simplepasswordstorage.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, UserDetailsPasswordService {
    boolean createUser(UserRegistrationDto userDto);

    User getCurrentUser();
}
