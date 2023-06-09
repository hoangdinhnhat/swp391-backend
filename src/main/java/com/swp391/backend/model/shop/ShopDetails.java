package com.swp391.backend.model.shop;

import com.swp391.backend.model.product.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ShopDetails {
    private ShopDTO shopDetails;
    private List<Product> products;
    private int maxPage;
}
