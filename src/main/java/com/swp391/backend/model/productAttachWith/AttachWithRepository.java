/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swp391.backend.model.productAttachWith;

import java.util.List;

import com.swp391.backend.model.categoryGroup.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Lenovo
 */
public interface AttachWithRepository extends JpaRepository<AttachWith, Integer>{
    List<AttachWith> findByGroupOne(CategoryGroup g);
    List<AttachWith> findByGroupTwo(CategoryGroup g);
}
