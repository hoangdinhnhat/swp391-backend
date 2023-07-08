package com.swp391.backend.model.categoryDetailInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CategoryDetailInfo {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "categoryDetailInfo")
    @JsonBackReference
    private List<ProductDetailInfo> productDetailInfos;

}
