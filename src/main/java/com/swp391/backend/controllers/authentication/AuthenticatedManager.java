/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
public class AuthenticatedManager {
    private UserDetails AUTHENTICATED_USER;
    
    public UserDetails getAuthenticatedUser()
    {
        return AUTHENTICATED_USER;
    }
    
    public void setAuthenticatedUser(UserDetails user)
    {
        AUTHENTICATED_USER = user;
    }
}
