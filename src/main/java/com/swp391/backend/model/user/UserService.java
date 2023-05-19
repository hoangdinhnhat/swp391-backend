/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }
    
    public UserDetails loadUserByUsername(String username, String filter) throws UsernameNotFoundException {
        User user = (User) repository.findByEmail(username)
                .orElse(null);
        if(user == null)
        {
            return null;
        }
        return user;
    }
    
    public UserDetails save(User user)
    {
        return repository.save(user);
    }
    
    public boolean isExist(String email)
    {
        return repository.findByEmail(email).isPresent();
    }
    
    public void enableUser(User user)
    {
        user.setEnabled(true);
        repository.save(user);
    }
}
