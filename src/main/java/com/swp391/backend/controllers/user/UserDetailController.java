/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.utils.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private final StorageService storageService;

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

    @PostMapping("/upload/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        storageService.store(file, user.getId() + ".avatar");
        return ResponseEntity.ok("Ok");
    }
    
    @GetMapping("/avatar")
    public ResponseEntity<Resource> getAvatar() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        String avatarFile = user.getId() + ".avatar";
        Resource file = storageService.loadAsResource(avatarFile);
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
        header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
        return ResponseEntity.ok().headers(header).body(file);
    }
}
