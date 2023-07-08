package com.swp391.backend.model.cart;

import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart findById(Integer id) {
        return cartRepository.findById(id).orElse(null);
    }

    public Cart findByUser(User user) {
        return cartRepository.findByUser(user);
    }
}
