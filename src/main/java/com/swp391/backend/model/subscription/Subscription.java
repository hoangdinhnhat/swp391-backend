package com.swp391.backend.model.subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Subscription {

    @EmbeddedId
    SubscriptionId id;

    @ManyToOne
    @MapsId("shopId")
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    private Shop shop;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private Date subscriptionTime;
}
