package com.yurwar.simplepasswordstorage.model.service;

import com.yurwar.simplepasswordstorage.controller.dto.UserRegistrationDto;
import com.yurwar.simplepasswordstorage.model.entity.User;
import com.yurwar.simplepasswordstorage.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public boolean createUser(UserRegistrationDto userDto) {
        User userInDatabase = userRepository.findUserByUsername(userDto.getUsername());
        if (userInDatabase == null) {
            User user = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User updatePassword(UserDetails userDetails, String newPassword) {
        User user = userRepository.findUserByUsername(userDetails.getUsername());
        user.setPassword(newPassword);
        return user;
    }
}
