/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.productAttachWith;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
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
@Table(name = "attach_with")
public class AttachWith {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "group_one_id")
    @JsonBackReference
    private CategoryGroup groupOne;

    @ManyToOne
    @JoinColumn(name = "group_two_id")
    @JsonBackReference
    private CategoryGroup groupTwo;
}
