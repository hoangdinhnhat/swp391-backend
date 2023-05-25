/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.receiveinfo;

import com.swp391.backend.model.user.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author Lenovo
 */
public interface ReceiveInfoRepository extends PagingAndSortingRepository<ReceiveInfo, Integer>, JpaRepository<ReceiveInfo, Integer>{
    
    List<ReceiveInfo> findByUser(User user, Pageable pageable);
    List<ReceiveInfo> findByUser(User user);
}
