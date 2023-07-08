package com.swp391.backend.model.cart;

import com.swp391.backend.model.cartProduct.CartProductDTO;
import com.swp391.backend.model.shop.ShopDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartItem {
    private ShopDTO shop;
    private List<CartProductDTO> cartProducts;
    private double shippingFee;
}
