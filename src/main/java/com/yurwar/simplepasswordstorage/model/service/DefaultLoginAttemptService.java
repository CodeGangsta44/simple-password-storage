package com.yurwar.simplepasswordstorage.model.service;


import org.springframework.stereotype.Service;

@Service
public class DefaultLoginAttemptService implements LoginAttemptService {
    private static final int MAX_ATTEMPT = 10;

    @Override
    public void loginSucceeded(String key) {

    }

    @Override
    public void loginFailed(String key) {

    }

    @Override
    public boolean isBlocked(String key) {
        return false;
    }
}
