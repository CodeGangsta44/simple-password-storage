package com.yurwar.simplepasswordstorage.model.service;

import com.yurwar.simplepasswordstorage.controller.dto.UserRegistrationDto;
import com.yurwar.simplepasswordstorage.model.entity.User;
import com.yurwar.simplepasswordstorage.model.repository.UserRepository;
import com.yurwar.simplepasswordstorage.utils.BlockedIpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Service
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final LoginAttemptService loginAttemptService;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                              LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
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
        String userIp = getClientIp();
        if (loginAttemptService.isBlocked(userIp)) {
            throw new BlockedIpException(userIp);
        }

        return userRepository.findUserByUsername(username);
    }

    private String getClientIp() {
        HttpServletRequest request =
                ((ServletRequestAttributes) requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        String forwarderForHeader = request.getHeader("X-Forwarded-For");
        if (isNull(forwarderForHeader)) {
            return request.getRemoteAddr();
        }
        return forwarderForHeader.split(",")[0];
    }

    @Override
    public User updatePassword(UserDetails userDetails, String newPassword) {
        User user = userRepository.findUserByUsername(userDetails.getUsername());
        user.setPassword(newPassword);
        return user;
    }

    @Override
    public User getCurrentUser() {

        return ((User)(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }
}
