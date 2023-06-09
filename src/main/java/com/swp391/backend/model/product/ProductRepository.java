/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.product;

import java.util.List;

import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.shop.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Lenovo
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, JpaRepository<Product, Integer>{
    List<Product> findByCategoryGroup(CategoryGroup cg, Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(String search, Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(String search);
    List<Product> findAll(Sort sort);

    List<Product> findByShop(Shop shop, Pageable pageable);
    List<Product> findByShop(Shop shop);
}
