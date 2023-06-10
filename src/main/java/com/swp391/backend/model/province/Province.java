package com.swp391.backend.model.province;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.shopAddress.ShopAddress;
import jakarta.persistence.*;
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
    @JsonBackReference
    private List<ShopAddress> shopAddresses;
}
