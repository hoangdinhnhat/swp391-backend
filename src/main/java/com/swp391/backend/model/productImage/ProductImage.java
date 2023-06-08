package com.swp391.backend.model.productImage;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductImage {
    @Id
    @GeneratedValue
    private Integer id;
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
}
