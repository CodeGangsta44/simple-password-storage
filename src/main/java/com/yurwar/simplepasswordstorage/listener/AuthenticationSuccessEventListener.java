package com.yurwar.simplepasswordstorage.listener;

import com.yurwar.simplepasswordstorage.model.service.LoginAttemptService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final LoginAttemptService loginAttemptService;

    public AuthenticationSuccessEventListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails)
                event.getAuthentication().getDetails();

        loginAttemptService.loginSucceeded(auth.getRemoteAddress());
    }
}
