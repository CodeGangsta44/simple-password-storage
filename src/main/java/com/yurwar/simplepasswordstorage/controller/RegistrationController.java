package com.yurwar.simplepasswordstorage.controller;

import com.yurwar.simplepasswordstorage.controller.dto.UserRegistrationDto;
import com.yurwar.simplepasswordstorage.model.service.UserService;
import com.yurwar.simplepasswordstorage.utils.CredentialValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private static final String usernameRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    //Passwords will contain at least 1 upper case letter
    //Passwords will contain at least 1 lower case letter
    //Passwords will contain at least 1 number or special character
    //Passwords will contain at least 8 characters in length
    //Password maximum length should not be arbitrarily limited
    private static final String passwordRegex = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Boolean> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        validate(userRegistrationDto);
        boolean isCreated = userService.createUser(userRegistrationDto);
        if (isCreated) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }

    private void validate(UserRegistrationDto userRegistrationDto) {
        boolean validUsername = Pattern.matches(usernameRegex, userRegistrationDto.getUsername());
        boolean validPassword = Pattern.matches(passwordRegex, userRegistrationDto.getPassword());

        if (!validPassword || !validUsername) {
            throw new CredentialValidationException("Credential is not valid");
        }
    }
}
