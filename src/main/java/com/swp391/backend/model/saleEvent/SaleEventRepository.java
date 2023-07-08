/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swp391.backend.model.saleEvent;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Lenovo
 */
public interface SaleEventRepository extends JpaRepository<SaleEvent, Integer> {
    SaleEvent findByName(String name);
}
