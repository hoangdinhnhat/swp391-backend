package com.swp391.backend.model.ward;

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
public class Ward {
    @Id
    private String id;
    private String name;

    @OneToMany(mappedBy = "ward")
    @JsonBackReference
    private List<ReceiveInfo> receiveInfos;

    @OneToMany(mappedBy = "ward")
    @JsonBackReference(value = "shopAddress")
    private List<ShopAddress> shopAddresses;
}
