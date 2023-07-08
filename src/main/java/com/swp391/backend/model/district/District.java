package com.swp391.backend.model.district;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.shopAddress.ShopAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class District {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "district")
    @JsonBackReference
    private List<ReceiveInfo> receiveInfos;

    @OneToMany(mappedBy = "district")
    @JsonBackReference(value = "shopAddress")
    private List<ShopAddress> shopAddresses;
}
