/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import com.swp391.backend.model.user.Gender;
import com.swp391.backend.model.user.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Lenovo
 */
public class AuthenticationControllerTest {

    private String URL;
    private HttpHeaders headers;
    private RestTemplate restTemplate;

    public AuthenticationControllerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        URL = "http://localhost:8080/api/v1/auths/registration";
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("accept", MediaType.APPLICATION_JSON_VALUE);
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of registration method, of class AuthenticationController.
     */
    @Test
    public void testRegistration() throws Exception {
        System.out.println("registration");
        RegistrationRequest request = RegistrationRequest.builder()
                .firstname("Nhat")
                .lastname("Hoang Dinh")
                .email("hoangdinhnhat123@gmail.com")
                .password("1234")
                .gender(Gender.MALE)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<RegistrationRequest> entity = new HttpEntity<>(request, headers);
        RegistrationResponse response = restTemplate.postForObject(URL, entity, RegistrationResponse.class);
        System.out.println(response.getStatus());
        Assertions.assertThat(response.getStatus()).isEqualTo("Registered successfully. Please verify your email to activate your account!");
    }

    /**
     * Test of authentication method, of class AuthenticationController.
     */
    @Test
    public void testAuthentication() throws Exception {
        System.out.println("authentication");
        URL = "http://localhost:8080/api/v1/auths/authentication";
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("hoangdinhnhat123@gmail.com")
                .password("1234")
                .build();
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(URL, request, AuthenticationResponse.class);
        System.out.println(response.getBody().getToken());
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
    }
}
