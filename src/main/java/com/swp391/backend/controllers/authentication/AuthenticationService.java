/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import com.swp391.backend.model.token.Token;
import com.swp391.backend.model.token.TokenService;
import com.swp391.backend.model.user.Role;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.security.JwtService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticatedManager authenticatedManager;

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        User user = (User) userService.loadUserByUsername(request.getEmail());
        user.setLogout(false);
        userService.save(user);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Token authToken = tokenService.findByUserAndType(user, "auth");
        if (authToken != null) {
            if (jwtService.isTokenValid(authToken.getValue(), user)) {
                return AuthenticationResponse.builder()
                        .token(authToken.getValue())
                        .build();
            } else {
                tokenService.delete(authToken);
            }
        }
        user.setTimeout(new Date(System.currentTimeMillis() + 1000 * 10));
        userService.save(user);
        String jwtToken = jwtService.generateToken(user);
        LocalDateTime expiredAt = jwtService.extractExpiration(jwtToken)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        authToken = Token.builder()
                .createAt(LocalDateTime.now())
                .expiredAt(expiredAt)
                .value(jwtToken)
                .user(user)
                .type("auth")
                .build();

        tokenService.save(authToken);
        authenticatedManager.setAuthenticatedUser(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public RegistrationResponse registration(RegistrationRequest request) {
        boolean isExist = userService.isExist(request.getEmail());
        if (isExist) {
            throw new IllegalStateException("Email is already taken!");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();
        userService.save(user);
        return RegistrationResponse.builder()
                .email(user.getEmail())
                .status("Registration Successfully")
                .build();
    }
    
    public String signout()
    {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        if(user == null)
        {
            return "Authentication First";
        }
        user.setLogout(true);
        userService.save(user);
        authenticatedManager.setAuthenticatedUser(null);
        return "Logout Successfully!";
    }

    public UserDTO resetFind(@RequestParam("email") String email) {
        User user = (User) userService.loadUserByUsername(email);
        return UserDTO.builder()
                .email(user.getEmail())
                .fullname(user.getFirstname() + " " + user.getLastname())
                .imageurl(user.getImageurl())
                .build();
    }

    public ResetResponse resetSend(UserDTO userDTO) {
        User user = (User) userService.loadUserByUsername(userDTO.getEmail());
        String forgetCode = Math.round((Math.random() * 899999 + 100000)) + "";
        System.out.println("Forget Code: " + forgetCode);
        String token = passwordEncoder.encode(forgetCode);
        Token resetToken = Token.builder()
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(60))
                .value(token)
                .user(user)
                .type("reset")
                .build();
        tokenService.save(resetToken);
        return ResetResponse.builder()
                .email(user.getEmail())
                .status("Send confirm code succesfully")
                .build();
    }

    public ResetResponse resetConfirm(UserDTO userDTO, String code) {
        String status = "Confirm Succesfully";
        User user = (User) userService.loadUserByUsername(userDTO.getEmail());
        Token resetToken = tokenService.findByUserAndType(user, "reset");
        if(!tokenService.isValid(resetToken))
        {
            status = "Confirm code is expired!";
        }
        boolean isMatching = passwordEncoder.matches(code, resetToken.getValue());
        if(!isMatching)
        {
            status = "Code isn't match";
        }
        return ResetResponse.builder()
                .email(user.getEmail())
                .status(status)
                .build();
    }
}
