package com.swp391.backend.model.productSale;

import com.swp391.backend.model.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductSaleDTO {

    private Product product;

    private Integer salePercent;

    private Integer saleQuantity;
    private int sold;
}
