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
public class ReceiveInfoRequest {
    private String fullname;
    private String phone;
    private Integer provinceId;
    private String provinceName;
    private Integer districtId;
    private String districtName;
    private String wardId;
    private String wardName;
    private String specific_address;
    private boolean _default;
}

