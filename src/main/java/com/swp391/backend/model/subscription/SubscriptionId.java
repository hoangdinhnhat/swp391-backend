package com.swp391.backend.model.subscription;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class SubscriptionId implements Serializable {

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "shop_id")
    Integer shopId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionId)) return false;
        SubscriptionId that = (SubscriptionId) o;
        return userId == that.userId &&
                shopId == that.shopId;
    }
}
