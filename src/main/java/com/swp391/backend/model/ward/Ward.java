package com.swp391.backend.model.ward;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Ward {
    @Id
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "ward")
    @JsonBackReference
    private List<ReceiveInfo> receiveInfos;

    @OneToMany(mappedBy = "ward")
    @JsonBackReference
    private List<ShopAddress> shopAddresses;
}