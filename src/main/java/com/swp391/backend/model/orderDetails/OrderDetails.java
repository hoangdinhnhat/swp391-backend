package com.swp391.backend.model.orderDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderDetails {

    @EmbeddedId
    OrderDetailsId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private Integer quantity;
    private double sellPrice;
    private double soldPrice;
    private boolean feedbacked;

    public OrderDetailsDTO toDto() {
        return OrderDetailsDTO.builder()
                .product(product)
                .quantity(quantity)
                .sellPrice(sellPrice)
                .soldPrice(soldPrice)
                .feedbacked(feedbacked)
                .build();
    }
}
