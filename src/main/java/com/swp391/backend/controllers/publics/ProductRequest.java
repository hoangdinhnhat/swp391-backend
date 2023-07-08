package com.swp391.backend.controllers.publics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ToString
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private Integer available;
    private Integer categoryGroup;
    private List<ProductDetailRequest> productDetailRequests;
}
