package com.swp391.backend.model.product;

import com.swp391.backend.model.productSale.ProductSaleDTO;
import com.swp391.backend.model.shop.ShopDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SearchDTO {
    private ShopDTO shop;
    private List<ProductSaleDTO> products;
    private int maxPage;
}
