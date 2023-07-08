/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import com.swp391.backend.model.user.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Builder
@AllArgsConstructor
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
