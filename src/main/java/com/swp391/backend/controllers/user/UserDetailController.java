/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserDetailController {

    private final AuthenticatedManager authenticatedManager;
    private final UserService userService;

//    @GetMapping("/{email}")
//    @PreAuthorize("#email == authentication.principal.username")
//    public UserDetails get(@PathVariable("email") String email) {
//        return userService.loadUserByUsername(email);
//    }
    
    @GetMapping("/info")
    public ResponseEntity<UserDTO> loginUserInfo() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .fullname(user.getFirstname() + " " + user.getLastname())
                .imageurl(user.getImageurl())
                .gender(user.getGender())
                .build();
        return ResponseEntity.ok().body(userDTO);
    }
}
