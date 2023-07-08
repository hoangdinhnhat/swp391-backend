/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Builder
@AllArgsConstructor
public class GoogleRequest {
    private String email;
    private String family_name;
    private String given_name;
    private String id;
    private String name;
    private String picture;
}
