/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.productSale;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.saleEvent.SaleEvent;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Lenovo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_sale")
public class ProductSale {

    @EmbeddedId
    ProductSaleKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @MapsId("saleEventId")
    @JoinColumn(name = "sale_event_id")
    @JsonBackReference
    private SaleEvent saleEvent;

    private Integer saleQuantity;
    private int sold;

    public ProductSaleDTO toDto() {
        return ProductSaleDTO.builder()
                .product(product)
                .saleQuantity(saleQuantity)
                .sold(sold)
                .salePercent(saleEvent.getPercent())
                .build();
    }
}
