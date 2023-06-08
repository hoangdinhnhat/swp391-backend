package com.swp391.backend.controllers.publics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private Integer available;
    private Integer sold;
    private double rating;
    private Integer categoryGroup;
}
