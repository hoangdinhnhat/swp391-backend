package com.swp391.backend.model.category;

import com.swp391.backend.model.productSale.ProductSaleDTO;
import com.swp391.backend.model.shop.ShopDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryDetails {
    private List<ShopDTO> shops;
    private String categoryName;
    private List<ProductSaleDTO> ps;
    private int maxPage;
}
