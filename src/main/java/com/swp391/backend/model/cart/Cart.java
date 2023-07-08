package com.swp391.backend.model.cart;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.cartProduct.CartProduct;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(mappedBy = "cart")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "cart")
    @JsonManagedReference
    private List<CartProduct> cartProducts;
}
