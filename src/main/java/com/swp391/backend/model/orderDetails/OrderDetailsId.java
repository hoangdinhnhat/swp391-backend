package com.swp391.backend.model.orderDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class OrderDetailsId implements Serializable {

    @Column(name = "order_id")
    String orderId;

    @Column(name = "product_id")
    Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDetailsId)) return false;
        OrderDetailsId that = (OrderDetailsId) o;
        return orderId.equals(that.orderId) &&
                productId == that.productId;
    }

}
