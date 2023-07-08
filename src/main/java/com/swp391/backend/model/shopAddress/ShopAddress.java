package com.swp391.backend.model.shopAddress;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.district.District;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.ward.Ward;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ShopAddress {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    @JsonManagedReference(value = "shopAddress")
    private Province province;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @JsonManagedReference(value = "shopAddress")
    private District district;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    @JsonManagedReference(value = "shopAddress")
    private Ward ward;

    private String specificAddress;

    @OneToMany
    @JsonBackReference
    private List<Shop> shops;
}
