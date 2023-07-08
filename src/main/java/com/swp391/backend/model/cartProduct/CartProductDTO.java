package com.swp391.backend.model.cartProduct;

import com.swp391.backend.model.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartProductDTO {

    private Product product;

    private int quantity;

    private int salePercent;
    private int saleQuantity;
    private int saleSold;
}
