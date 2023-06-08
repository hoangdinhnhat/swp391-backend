package com.swp391.backend.model.cartProduct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartProduct {
    @EmbeddedId
    CartProductKey id;

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cartId")
    @JsonBackReference
    Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productId")
    @JsonManagedReference
    Product product;

    Integer quantity;
}
