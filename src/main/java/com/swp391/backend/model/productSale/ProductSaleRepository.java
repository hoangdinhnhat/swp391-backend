/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swp391.backend.model.productSale;

import java.util.List;

import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.saleEvent.SaleEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Lenovo
 */
public interface ProductSaleRepository extends PagingAndSortingRepository<ProductSale, ProductSaleKey>, JpaRepository<ProductSale, ProductSaleKey>{
    List<ProductSale> findBySaleEvent(SaleEvent event, Pageable pageable);
    List<ProductSale> findBySaleEvent(SaleEvent event);
}
