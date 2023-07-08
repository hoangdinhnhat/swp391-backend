/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.categoryGroup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.productAttachWith.AttachWith;
import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "category_group")
public class CategoryGroup {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "categoryGroup")
    @JsonManagedReference
    private List<Product> products;

    @OneToMany(mappedBy = "groupOne")
    @JsonManagedReference
    private List<AttachWith> attachWithGroupOne;

    @OneToMany(mappedBy = "groupTwo")
    @JsonManagedReference
    private List<AttachWith> attachWithGroupTwo;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    public CategoryGroupDTO toDTO() {
        return CategoryGroupDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
}
