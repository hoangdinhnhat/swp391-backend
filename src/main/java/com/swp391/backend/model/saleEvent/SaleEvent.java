/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.saleEvent;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.productSale.ProductSale;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;

import lombok.*;

/**
 *
 * @author Lenovo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_event")
public class SaleEvent {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Date start;
    private Date endding;
    private int percent;
    
    @OneToMany(mappedBy = "saleEvent")
    @JsonManagedReference
    private List<ProductSale> product_sales;
}
