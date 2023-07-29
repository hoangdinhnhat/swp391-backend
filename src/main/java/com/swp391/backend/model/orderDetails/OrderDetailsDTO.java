package com.swp391.backend.model.orderDetails;

import com.swp391.backend.model.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDetailsDTO {
    private Product product;
    private Integer quantity;
    private double sellPrice;
    private double soldPrice;
    private boolean feedbacked;
}
