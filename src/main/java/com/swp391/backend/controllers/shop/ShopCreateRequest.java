package com.swp391.backend.controllers.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ShopCreateRequest {
    private String name;
    private Integer provinceId;
    private String provinceName;
    private Integer districtId;
    private String districtName;
    private String wardId;
    private String wardName;
    private String specific_address;
    private String email;
    private String phone;
}
