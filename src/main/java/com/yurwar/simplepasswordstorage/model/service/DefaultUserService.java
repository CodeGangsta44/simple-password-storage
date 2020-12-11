package com.yurwar.simplepasswordstorage.model.service;

import com.yurwar.simplepasswordstorage.controller.dto.UserRegistrationDto;
import com.yurwar.simplepasswordstorage.model.entity.User;
import com.yurwar.simplepasswordstorage.model.repository.EncryptedUserRepository;
import com.yurwar.simplepasswordstorage.utils.BlockedIpException;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Service
public class DefaultUserService implements UserService {

    private final EncryptedUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final LoginAttemptService loginAttemptService;

    public DefaultUserService(EncryptedUserRepository userRepository, PasswordEncoder passwordEncoder,
                              LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public boolean createUser(UserRegistrationDto userDto) {
        User userInDatabase = userRepository.findUserByUsername(userDto.getUsername());
        if (userInDatabase == null) {
            User user = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .address(userDto.getAddress())
                    .dek(generateDekForUser())
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

    private String generateDekForUser() {
        int DEK_SIZE = 256;
        String DEK_ALGORITHM = "AES";

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(DEK_ALGORITHM);
            keyGen.init(DEK_SIZE);

            SecretKey secretKey = keyGen.generateKey();
            byte[] encodedKey = secretKey.getEncoded();
            return new String(Hex.encode(encodedKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
