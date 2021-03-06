package com.expexchangeservice.service.impl;

import com.expexchangeservice.model.dto.UserDto;
import com.expexchangeservice.model.entities.Token;
import com.expexchangeservice.model.entities.User;
import com.expexchangeservice.model.entities.UserAuthentication;
import com.expexchangeservice.repository.interfaces.ITokenRepository;
import com.expexchangeservice.service.interfaces.ITokenService;
import com.expexchangeservice.service.interfaces.IUserService;
import com.expexchangeservice.utils.security.ITokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Service
public class TokenService implements ITokenService {

    private ITokenRepository tokenRepository;
    private ITokenHandler tokenHandler;
    private IUserService userService;

    @Autowired
    public TokenService(ITokenRepository tokenRepository, ITokenHandler tokenHandler, IUserService userService) {
        this.tokenRepository = tokenRepository;
        this.tokenHandler = tokenHandler;
        this.userService = userService;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String tokenValue = request.getHeader(AUTH_HEADER_NAME);
        if (tokenHandler.checkToken(tokenValue) && checkTokenInDatabase(tokenValue)) {
            User user = userService.loadUserById(tokenHandler.getUserIdFromToken(tokenValue));
            return new UserAuthentication(user);
        }
        return null;
    }

    @Override
    public Token generateToken(UserDto userDto) {
        User user = userService.loadUserByUsername(userDto.getUsername());
        if (user != null) {
            Token token = createToken(user);
            tokenRepository.create(token);
            return token;
        }
        return null;
    }

    @Override
    public boolean deleteToken(HttpServletRequest request) {
        String tokenValue = request.getHeader(AUTH_HEADER_NAME);
        Token token = tokenRepository.findByValue(tokenValue);
        if (token == null) {
            return false;
        }
        tokenRepository.deleteByUser(token.getUser());
        return true;
    }

    @Override
    public boolean checkUser(HttpServletRequest request, String username) {
        String tokenValue = request.getHeader(AUTH_HEADER_NAME);
        User user = userService.loadUserById(tokenHandler.getUserIdFromToken(tokenValue));
        return user.getUsername().equals(username);
    }

    @Override
    public boolean checkUser(HttpServletRequest request, Long userId) {
        String tokenValue = request.getHeader(AUTH_HEADER_NAME);
        Long id = tokenHandler.getUserIdFromToken(tokenValue);
        return id.equals(userId);
    }

    @Override
    public String getUsernameByRequest(HttpServletRequest request) {
        String tokenValue = request.getHeader(AUTH_HEADER_NAME);
        User user = userService.loadUserById(tokenHandler.getUserIdFromToken(tokenValue));
        return user.getUsername();
    }

    private Token createToken(User user) {
        return new Token(tokenHandler.generateToken(user.getId()), user);
    }

    private boolean checkTokenInDatabase(String value) {
        Token token = tokenRepository.findByValue(value);
        return token != null;
    }
}
