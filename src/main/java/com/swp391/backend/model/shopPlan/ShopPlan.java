package com.swp391.backend.model.shopPlan;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.shopPackage.ShopPackage;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopPlan {
    @Id
    private String plan;
    private Integer duration;
    private float price;

    @OneToMany(mappedBy = "shopPlan")
    @JsonBackReference
    private List<ShopPackage> shopPackages;
}
