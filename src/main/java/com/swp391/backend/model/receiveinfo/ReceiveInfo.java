/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.receiveinfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.district.District;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.ward.Ward;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "receive_info")
public class ReceiveInfo {
    
    @Id
    @GeneratedValue
    private Integer id;
    private String fullname;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    @JsonManagedReference
    private Province province;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @JsonManagedReference
    private District district;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    @JsonManagedReference
    private Ward ward;

    private String specific_address;
    private boolean _default;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
