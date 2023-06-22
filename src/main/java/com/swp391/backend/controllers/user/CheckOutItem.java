package com.swp391.backend.controllers.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckOutItem {
    private Integer productId;
    private int quantity;
    private int salePercent;
    private int saleQuantity;
    private int saleSold;
}
