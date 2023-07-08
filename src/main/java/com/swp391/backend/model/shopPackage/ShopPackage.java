package com.swp391.backend.model.shopPackage;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shopPlan.ShopPlan;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ShopPackage {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shop_plan_id")
    private ShopPlan shopPlan;

    private Date startTime;
    private Date endTime;
    private Integer duration;
}
