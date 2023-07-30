/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.security;

import com.google.api.client.util.DateTime;
import com.swp391.backend.model.counter.CounterService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserRepository;
import com.swp391.backend.model.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Lenovo
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CounterService counterService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = null;
        for (Cookie cooky : cookies) {
            if (cooky.getName().equals("Authorization")) {
                authHeader = cooky.getValue();
            }
        }
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer_")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(userEmail, "filter");
            if (userDetails == null) {
                filterChain.doFilter(request, response);
                return;
            }
            if (jwtService.isTokenValid(jwt, userDetails)) {
                User appUser = (User) userDetails;
                LocalDateTime localDateTime = LocalDateTime.now();
                localDateTime = localDateTime.plusMinutes(120);
                Date newTimeOut = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                appUser.setTimeout(newTimeOut);

                var counter = counterService.getById("VISIT_PAGE");
                counter.setValue(counter.getValue() + 1);
                counterService.save(counter);

                userRepository.save(appUser);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
