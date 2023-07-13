/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import com.swp391.backend.model.user.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService service;

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) throws Exception {
        AuthenticationResponse authRes = service.authentication(request);
        final ResponseCookie responseCookie = ResponseCookie
                .from("Authorization", "Bearer_" + authRes.getToken())
                .httpOnly(true)
                .path("/")
                .maxAge(1800)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok().body(authRes);
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> google(
            @RequestBody GoogleRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authRes = service.google(request);
        final ResponseCookie responseCookie = ResponseCookie
                .from("Authorization", "Bearer_" + authRes.getToken())
                .httpOnly(true)
                .path("/")
                .maxAge(1800)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok().body(authRes);
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registration(@RequestBody RegistrationRequest request) throws Exception {
        return ResponseEntity.ok().body(service.registration(request));
    }

    @GetMapping("/registration/confirm")
    public ResponseEntity<RegistrationResponse> registrationConfirm(@RequestParam("token") String token, RedirectAttributes attributes) {
        return ResponseEntity.ok().body(service.emailConfirm(token));
    }

    @GetMapping("/registration/resend")
    public ResponseEntity<RegistrationResponse> registrationResend(@RequestParam("token") String oldToken) throws Exception {
        return ResponseEntity.ok().body(service.emailResend(oldToken));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(HttpServletRequest request, HttpServletResponse response) {
        final ResponseCookie responseCookie = ResponseCookie
                .from("Authorization", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return ResponseEntity.ok().body(service.signout());
    }

    @GetMapping("/reset/find")
    public ResponseEntity<UserDTO> restFind(@RequestParam("email") String email) {
        return ResponseEntity.ok().body(service.resetFind(email));
    }

    @PostMapping("/reset/send")
    public ResponseEntity<ResetResponse> resetSend(@RequestParam("email") String email) throws Exception {
        return ResponseEntity.ok().body(service.resetSend(email));
    }

    @PostMapping("/reset/confirm")
    public ResponseEntity<ResetResponse> resetConfirm(@RequestParam("email") String email, @RequestParam("code") String code) {
        return ResponseEntity.ok().body(service.resetConfirm(email, code));
    }

    @PostMapping("/reset/new")
    public ResponseEntity<ResetResponse> resetNew(@RequestParam("email") String email, @RequestParam("password") String password) {
        return ResponseEntity.ok().body(service.resetNew(email, password));
    }
}
