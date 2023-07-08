package com.swp391.backend.model.cartProduct;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CartProductKey implements Serializable {
    @Column(name = "cart_id")
    public Integer cartId;

    @Column(name = "product_id")
    public Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartProductKey)) return false;
        CartProductKey that = (CartProductKey) o;
        return cartId == that.cartId &&
                productId == that.productId;
    }
}
