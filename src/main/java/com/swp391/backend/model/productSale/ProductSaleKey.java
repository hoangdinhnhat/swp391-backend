package com.swp391.backend.model.productSale;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ProductSaleKey implements Serializable {
    @Column(name = "sale_event_id")
    Integer saleEventId;

    @Column(name = "product_id")
    Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSaleKey)) return false;
        ProductSaleKey that = (ProductSaleKey) o;
        return saleEventId == that.saleEventId &&
                productId == that.productId;
    }
}
