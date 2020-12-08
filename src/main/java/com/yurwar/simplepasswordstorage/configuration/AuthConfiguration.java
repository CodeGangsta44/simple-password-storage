package com.yurwar.simplepasswordstorage.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.Map;

@Configuration
public class AuthConfiguration {
    //Argon2 configuration constants
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 64;
    private static final int MEMORY = 4096;
    private static final int ITERATIONS = 6;
    //BCrypt configuration constants
    private static final int STRENGTH = 15;

    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = Map.of(
                "argon2", new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PROCESSORS, MEMORY, ITERATIONS),
                "bcrypt", new BCryptPasswordEncoder(STRENGTH, new SecureRandom()));
        return new DelegatingPasswordEncoder("argon2", encoders);
    }
}
