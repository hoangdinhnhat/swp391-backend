package com.swp391.backend.model.cartProduct;

import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository;

    public List<CartProduct> getCartProductByCart(Cart cart, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return cartProductRepository.findByCart(cart, pageable);
    }

    public CartProduct save(CartProduct cartProduct) {
        return cartProductRepository.save(cartProduct);
    }

    public void delete(CartProductKey id) {
        cartProductRepository.deleteById(id);
    }

    public CartProduct findByCartAndProduct(Cart cart, Product product) {
        return cartProductRepository.findByCartAndProduct(cart, product).orElse(null);
    }
}
