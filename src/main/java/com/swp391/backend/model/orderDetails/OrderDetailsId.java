package com.swp391.backend.model.orderDetails;

import com.swp391.backend.model.subscription.SubscriptionId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class OrderDetailsId implements Serializable {

    @Column(name = "order_id")
    Integer orderId;

    @Column(name = "product_id")
    Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailsId)) return false;
        OrderDetailsId that = (OrderDetailsId) o;
        return orderId == that.orderId &&
                productId == that.productId;
    }

}
