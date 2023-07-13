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
import com.swp391.backend.utils.mail.ConfirmCodeTemplete;
import com.swp391.backend.utils.mail.EmailSender;
import com.swp391.backend.utils.mail.ForgetCodeTemplete;
import com.swp391.backend.utils.mail.SecurityConfirmTemplete;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

/**
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailSender gmailSender;
    private final AuthenticatedManager authenticatedManager;

    public AuthenticationResponse authentication(AuthenticationRequest request) throws Exception {
        if (authenticatedManager.getAuthenticatedUser() != null) {
            throw new IllegalStateException("Please log out of the currently logged in account first!");
        }
        User user = (User) userService.loadUserByUsername(request.getEmail());
        user.setLogout(false);
        userService.save(user);
        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!isMatch) {
            user.setWrongpasswordcounter(user.getWrongpasswordcounter() + 1);
            int counter = user.getWrongpasswordcounter();
            userService.save(user);
            if (counter == 3) {
                throw new IllegalStateException("You have entered the wrong password 3 times in a row. If you enter incorrectly more than 5 times in a row, your account will be temporarily locked!");
            } else if (counter == 6) {
                disableAccountAndSendMail(user);
                throw new IllegalStateException("Your account has been temporarily locked. We have sent a confirmation message to your email. Please check your email to confirm your account is still safe.");
            }
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        user.setWrongpasswordcounter(0);
        Token authToken = tokenService.findByUserAndType(user, "auth");
        if (authToken != null) {
            if (jwtService.isTokenValid(authToken.getValue(), user)) {
                return AuthenticationResponse.builder()
                        .token(authToken.getValue())
                        .role(user.getRole().name())
                        .build();
            } else {
                tokenService.delete(authToken);
            }
        }
        user.setTimeout(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();
    }

    private void disableAccountAndSendMail(User user) throws Exception {
        user.setEnabled(false);
        Token authToken = tokenService.findByUserAndType(user, "security");
        if (authToken != null) {
            tokenService.delete(authToken);
        }
        String confToken = UUID.randomUUID().toString();
        Token registration = Token.builder()
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .value(confToken)
                .user(user)
                .type("security")
                .build();
        tokenService.save(registration);
        String templete = SecurityConfirmTemplete.getTemplete("Bird Trading Platform", user.getFirstname(), "http://localhost:3000/confirm?token=" + confToken);
        gmailSender.send("Secure Confirmation", templete, user.getEmail());
        userService.save(user);
    }

    public AuthenticationResponse google(GoogleRequest request) {
        String email = request.getEmail();
        User user = (User) userService.loadUserByUsername(email, "google");
        Token authToken;
        if (user != null) {
            user.setLogout(false);
            userService.save(user);

            authToken = tokenService.findByUserAndType(user, "auth");
            if (authToken != null) {
                if (jwtService.isTokenValid(authToken.getValue(), user)) {
                    return AuthenticationResponse.builder()
                            .token(authToken.getValue())
                            .build();
                } else {
                    tokenService.delete(authToken);
                }
            }
        } else {
            String lastName = request.getFamily_name() == null ? "" : request.getFamily_name();
            user = User.builder()
                    .firstname(request.getGiven_name())
                    .lastname(lastName)
                    .email(request.getEmail())
                    .gender(null)
                    .imageurl(request.getPicture())
                    .enabled(true)
                    .locked(false)
                    .password(null)
                    .role(Role.CUSTOMER)
                    .joinAt(new Date())
                    .build();
        }
        UsernamePasswordAuthenticationToken usernameToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernameToken);

        user.setTimeout(new Date(System.currentTimeMillis() + 1000 * 60 * 30));
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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public RegistrationResponse registration(RegistrationRequest request) throws Exception {
        boolean isExist = userService.isExist(request.getEmail());
        if (isExist) {
            throw new IllegalStateException("Email is already taken!");
        }
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .gender(request.getGender())
                .enabled(false)
                .locked(false)
                .imageurl("/api/v1/users/info/avatar")
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .joinAt(new Date())
                .build();
        userService.save(user);
        String confToken = UUID.randomUUID().toString();
        Token registration = Token.builder()
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .value(confToken)
                .user(user)
                .type("regis")
                .build();
        tokenService.save(registration);
        String templete = ConfirmCodeTemplete.getTemplete("Bird Trading Platform", user.getFirstname(), "http://localhost:3000/confirm?token=" + confToken);
        gmailSender.send("Registration Confirmation", templete, user.getEmail());

        return RegistrationResponse.builder()
                .email(user.getEmail())
                .status("Registered successfully. Please verify your email to activate your account!")
                .build();
    }

    public RegistrationResponse emailConfirm(String token) {
        Token confToken = tokenService.findByValue(token);

        if (confToken == null) {
            throw new IllegalStateException("Token not found!");
        }

        if (confToken.getConfirmedAt() != null) {
            throw new IllegalStateException("You're already confirmed");
        }

        LocalDateTime expiredAt = confToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }
        confToken.setConfirmedAt(LocalDateTime.now());
        tokenService.save(confToken);
        userService.enableUser(confToken.getUser());
        return RegistrationResponse.builder()
                .email(confToken.getUser().getEmail())
                .status("Verify email successfully.")
                .build();
    }

    public RegistrationResponse emailResend(String oldToken) throws Exception {
        Token confToken = tokenService.findByValue(oldToken);

        if (confToken == null) {
            throw new IllegalStateException("Token not found!");
        }

        if (confToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        if (confToken.getExpiredAt().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("The email was sent within minutes. Please double-check your email.");
        }

        User user = confToken.getUser();

        tokenService.delete(confToken);

        String randomToken = UUID.randomUUID().toString();
        Token registration = Token.builder()
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .value(randomToken)
                .user(user)
                .type("regis")
                .build();
        tokenService.save(registration);

        String templete = ConfirmCodeTemplete.getTemplete("Bird Trading Platform", user.getFirstname(), "http://localhost:3000/confirm?token=" + randomToken);
        gmailSender.send("Registration Confirmation", templete, user.getEmail());

        return RegistrationResponse.builder()
                .email(user.getEmail())
                .status("Resend email succesfully. Please verify your email to activate your account!")
                .build();
    }

    public String signout() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        if (user == null) {
            return "Authentication First";
        }
        user.setLogout(true);
        userService.save(user);
        return "Logout Successfully!";
    }

    public UserDTO resetFind(@RequestParam("email") String email) {
        User user = (User) userService.loadUserByUsername(email);
        return UserDTO.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .imageurl(user.getImageurl())
                .build();
    }

    public ResetResponse resetSend(String email) throws Exception {
        User user = (User) userService.loadUserByUsername(email);
        Token checkToken = tokenService.findByUserAndType(user, "reset");
        if (checkToken != null) {
            tokenService.delete(checkToken);
        }
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
        String templete = ForgetCodeTemplete.getTemplete("Bird Trading Platform", user.getFirstname(), forgetCode);
        gmailSender.send("Forget Password Confirmation", templete, user.getEmail());
        return ResetResponse.builder()
                .email(user.getEmail())
                .status("Send confirm code succesfully")
                .build();
    }

    public ResetResponse resetConfirm(String email, String code) {
        String status = "Confirm Succesfully";
        User user = (User) userService.loadUserByUsername(email);
        Token resetToken = tokenService.findByUserAndType(user, "reset");
        if (!tokenService.isValid(resetToken)) {
            status = "Confirm code is expired!";
            throw new IllegalStateException(status);
        }
        boolean isMatching = passwordEncoder.matches(code, resetToken.getValue());
        if (!isMatching) {
            status = "Code isn't match";
            throw new IllegalStateException(status);
        }
        return ResetResponse.builder()
                .email(user.getEmail())
                .status(status)
                .build();
    }

    public ResetResponse resetNew(String email, String newPass) {
        String status = "Reset Password Succesfully";
        User user = (User) userService.loadUserByUsername(email);
        String newPassword = passwordEncoder.encode(newPass);
        user.setPassword(newPassword);
        userService.save(user);
        return ResetResponse.builder()
                .email(user.getEmail())
                .status(status)
                .build();
    }
}
