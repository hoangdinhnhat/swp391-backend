/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
public class AuthenticatedManager {
    
    private final UserService userService;
    
    public UserDetails getAuthenticatedUser()
    {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();
        return userService.loadUserByUsername(email, "filter");
    }
}
