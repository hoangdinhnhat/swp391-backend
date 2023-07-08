package com.swp391.backend.controllers.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopResponseRequest {
    private Integer feedbackId;
    private String response;
}
