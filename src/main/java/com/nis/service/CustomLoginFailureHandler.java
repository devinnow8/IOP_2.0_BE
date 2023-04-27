package com.nis.service;

import com.nis.entity.UserDetails;
import com.nis.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        LoginRequest loginRequest = (LoginRequest)request;
        System.out.println("implementing failure:"+loginRequest.getUsername_email());
        UserDetails user = null;
        try {
            user = userService.getUserByUsernameOrEmail(loginRequest.getUsername_email());
        } catch (Exception ex) {

        }

        if (user != null) {
            if (!user.isLocked()) {
                if (user.getFailedAttempt() < 3 ) {
//                    userService.increaseFailedAttempts(user);
                } else {
//                    userService.lock(user);
//                    exception = new LockedException("Your account has been locked due to 3 failed attempts.");
                }
            }

        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
