package com.swp391.backend.model.cartProduct;

import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends PagingAndSortingRepository<CartProduct, CartProductKey>, JpaRepository<CartProduct, CartProductKey> {
    List<CartProduct> findByCart(Cart cart, Pageable pageable);

    Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);
}
