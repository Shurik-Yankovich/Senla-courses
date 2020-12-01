package com.expexchangeservice.controller.config;

import com.expexchangeservice.utils.security.UserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class CustomLogoutHandler implements LogoutHandler {

    public CustomLogoutHandler() {
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        String userName = UserUtils.getAuthenticatedUserName();
        UserKeeper.deleteLoggedUser();
    }
}
