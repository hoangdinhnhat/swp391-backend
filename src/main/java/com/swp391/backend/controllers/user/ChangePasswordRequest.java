/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Builder
@AllArgsConstructor
public class ChangePasswordRequest {
    private String oldpassword;
    private String newpassword;
}
