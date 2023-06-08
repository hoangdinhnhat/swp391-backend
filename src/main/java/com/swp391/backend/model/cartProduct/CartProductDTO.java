package com.swp391.backend.model.cartProduct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.product.Product;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;

@Getter
@Setter
@Builder
public class CartProductDTO {

    private Product product;

    private int quantity;

    private int salePercent;
    private int saleQuantity;
    private int saleSold;
}
