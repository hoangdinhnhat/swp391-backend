/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import com.swp391.backend.model.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthenticationController {
    
    @Autowired
    private final AuthenticationService service;
    
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok().body(service.authentication(request));
    }
    
    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registration(@RequestBody RegistrationRequest request)
    {
        return ResponseEntity.ok().body(service.registration(request));
    }
    
    @GetMapping("/reset/find")
    public ResponseEntity<UserDTO> restFind(@RequestParam("email") String email)
    {
        return ResponseEntity.ok().body(service.restFind(email));
    }
    
    @PostMapping("/reset/send")
    public ResponseEntity<ResetResponse> resetSend(@RequestBody UserDTO userDTO)
    {
        return ResponseEntity.ok().body(service.resetSend(userDTO));
    }
    
    @PostMapping("/reset/confirm")
    public ResponseEntity<ResetResponse> resetConfirm(@RequestBody UserDTO userDTO, @RequestParam("code") String code)
    {
        return ResponseEntity.ok().body(service.resetConfirm(userDTO, code));
    }
}
