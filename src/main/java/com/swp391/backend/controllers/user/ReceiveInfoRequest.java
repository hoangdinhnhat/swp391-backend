/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Lenovo
 */
@Data
@Builder
@AllArgsConstructor
public class ReceiveInfoRequest {
    private String fullname;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String specific_address;
    private boolean _default;
}

