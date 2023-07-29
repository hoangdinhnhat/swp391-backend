/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
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

    public UserDetails getById(Integer id) {
        UserDetails user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User ID not found"));

        return user;
    }

    public UserDetails loadUserByUsername(String username, String filter) throws UsernameNotFoundException {
        User user = (User) repository.findByEmail(username)
                .orElse(null);
        if (user == null) {
            return null;
        }
        return user;
    }

    public UserDetails save(User user) {
        return repository.save(user);
    }

    public boolean isExist(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public void enableUser(User user) {
        user.setEnabled(true);
        repository.save(user);
    }

    public Integer getNewUserInMonth() {
        Integer newUsers = repository.getNewUserInMonth();
        if (newUsers == null) newUsers = 0;
        return newUsers;
    }

    public List<User> getAllUser() {
        Pageable pageable = PageRequest.of(0, 666, Sort.by("joinAt").descending());
        return repository.findAll(pageable).getContent();
    }

    public void init() {
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
                .joinAt(new Date())
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
                .joinAt(new Date())
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
                .joinAt(new Date())
                .build();
        save(user3);

        var user4 = User.builder()
                .firstname("Musk")
                .lastname("Elon")
                .email("elon@musk.com")
                .gender(Gender.MALE)
                .enabled(true)
                .locked(false)
                .imageurl("/api/v1/publics/user/avatar/elon@musk.com")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .role(Role.ADMIN)
                .joinAt(new Date())
                .build();
        save(user4);

        var user5 = User.builder()
                .firstname("GHN")
                .lastname("GHN")
                .email("GHN@gmail.com")
                .gender(Gender.MALE)
                .enabled(true)
                .locked(false)
                .imageurl("/api/v1/publics/user/avatar/GHN@gmail.com")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .role(Role.SHIPPING_UNIT)
                .joinAt(new Date())
                .build();
        save(user5);
    }
}
