/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.token;

import com.swp391.backend.model.user.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    
    @Autowired
    private final TokenRepository tokenRepository;
    public Token findByUserAndType(User user, String type)
    {
        Token token = tokenRepository.findByUserAndType(user, type)
                .orElse(null);
        return token;
    }

    public void delete(Token authToken) {
        tokenRepository.delete(authToken);
    }

    public Token save(Token authToken) {
        return tokenRepository.save(authToken);
    }
    
    public boolean isValid(Token token) {
        return !token.getExpiredAt().isBefore(LocalDateTime.now());
    }
}