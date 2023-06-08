package com.swp391.backend.model.product;

import com.swp391.backend.model.category.CategoryDTO;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.categoryGroup.CategoryGroupDTO;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackDTO;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleDTO;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import jakarta.persistence.Lob;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private double price;
    private double rating;
    private int available;
    private int sold;
    private String video;
    private CategoryDTO category;
    private CategoryGroupDTO categoryGroup;
    private List<FeedbackDTO> feedbacks;
    private List<ProductImage> images;
    private ProductSaleDTO productSale;
    private List<ProductDetailInfo> productDetailInfos;
    private ShopDTO shop;
    private List<Product> attachWiths;
}
