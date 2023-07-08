package com.swp391.backend.model.province;

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
public class Province {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "province")
    @JsonBackReference
    private List<ReceiveInfo> receiveInfos;

    @OneToMany(mappedBy = "province")
    @JsonBackReference(value = "shopAddress")
    private List<ShopAddress> shopAddresses;
}
