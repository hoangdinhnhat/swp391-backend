/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.cartProduct.CartProduct;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.shop.Shop;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author Lenovo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private int available;
    private int sold;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductImage> images;
    private String video;
    private double rating;
    private Date uploadTime;

    @ManyToOne
    @JoinColumn(name = "category_group_id")
    @JsonBackReference
    private CategoryGroup categoryGroup;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<ProductSale> productSales;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductDetailInfo> productDetailInfos;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CartProduct> cartProducts;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    private Shop shop;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<Report> reports;

    private boolean ban;

    public double getRating() {
        double rating = 0;
        double size = 0;
        var filteredFeedback = feedbacks.stream().filter(f -> !f.getType().startsWith("REPORT")).toList();
        for (var fb : filteredFeedback) {
            rating += fb.getRate();
            size += 1;
        }
        if (size == 0) {
            rating = 5;
        } else rating = rating / size;

        return rating;
    }

    @PrePersist
    @PreUpdate
    private void checkConstraint() {
        if (available < 0) {
            throw new IllegalStateException("The product available isn't enough");
        }
    }
}
