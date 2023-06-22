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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public UserDetails getById(Integer id)
    {
        UserDetails user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User ID not found"));

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

    public void init()
    {
        var user1 = User.builder()
                .firstname("Bao")
                .lastname("Tran Thien Thanh")
                .email("tranthienthanhbao@gmail.com")
                .gender(Gender.MALE)
                .enabled(true)
                .locked(false)
                .imageurl("/api/v1/publics/user/avatar/tranthienthanhbao@gmail.com")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .role(Role.CUSTOMER)
                .build();
        save(user1);

        var user2 = User.builder()
                .firstname("Thien")
                .lastname("Vu Duc")
                .email("vuducthien@gmail.com")
                .gender(Gender.MALE)
                .enabled(true)
                .locked(false)
                .imageurl("/api/v1/publics/user/avatar/vuducthien@gmail.com")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .role(Role.CUSTOMER)
                .build();
        save(user2);

        var user3 = User.builder()
                .firstname("Nhat")
                .lastname("Hoang Dinh")
                .email("nhathdse160377@fpt.edu.vn")
                .gender(Gender.MALE)
                .enabled(true)
                .locked(false)
                .imageurl("/api/v1/publics/user/avatar/nhathdse160377@fpt.edu.vn")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .role(Role.CUSTOMER)
                .build();
        save(user3);
    }
}
