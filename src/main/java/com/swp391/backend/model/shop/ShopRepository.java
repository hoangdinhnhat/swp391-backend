/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.shop;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ShopRepository extends PagingAndSortingRepository<Shop, Integer>, JpaRepository<Shop, Integer>{
    List<Shop> findByNameLike(String search, Pageable pageable);
    List<Shop> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
