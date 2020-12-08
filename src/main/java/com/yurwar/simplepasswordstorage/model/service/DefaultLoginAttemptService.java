package com.yurwar.simplepasswordstorage.model.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultLoginAttemptService implements LoginAttemptService {
    private static final int MAX_ATTEMPT = 10;
    private final LoadingCache<String, Integer> currentAttempts;

    public DefaultLoginAttemptService() {
        currentAttempts = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.DAYS)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    @Override
    public void loginSucceeded(String key) {
        currentAttempts.invalidate(key);
    }

    @Override
    public void loginFailed(String key) {
        int attempts = getAttempts(key);
        attempts++;
        currentAttempts.put(key, attempts);
    }

    private int getAttempts(String key) {
        int attempts = 0;
        try {
            attempts = currentAttempts.get(key);
        } catch (ExecutionException e) {
            //Ignore
        }
        return attempts;
    }

    @Override
    public boolean isBlocked(String key) {
        return getAttempts(key) >= MAX_ATTEMPT;
    }
}
