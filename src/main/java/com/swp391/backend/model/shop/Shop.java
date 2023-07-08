/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.shop;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.shopAddress.ShopAddress;
import com.swp391.backend.model.shopPackage.ShopPackage;
import com.swp391.backend.model.subscription.Subscription;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author Lenovo
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shop {

    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    List<Product> products;
    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    List<Notification> notifications;
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private double rating;
    private Date joinTime;
    private String shopImage;
    private String phone;
    private double wallet;
    private boolean ban;
    private int numberOfWarning;
    @ManyToOne
    @JoinColumn(name = "shop_address_id")
    @JsonManagedReference
    private ShopAddress shopAddress;
    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    private List<Subscription> subscriptions;
    @OneToMany(mappedBy = "shop")
    @JsonManagedReference
    private List<Order> orders;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "shop")
    @JsonBackReference
    private List<ConversationChatter> conversationChatters;

    @OneToMany(mappedBy = "shop")
    @JsonBackReference
    private List<ShopPackage> shopPackages;

    @OneToMany(mappedBy = "shop")
    @JsonBackReference
    private List<Report> reports;

    public double getRating() {
        double rating = 0;
        double size = 0;
        for (var fb : products) {
            rating += fb.getRating();
            size += 1;
        }
        if (size == 0) {
            rating = 5;
        } else rating = rating / size;
        return rating;
    }

    public ShopDTO toDto() {
        return ShopDTO.builder()
                .id(id)
                .name(name)
                .shopImage(shopImage)
                .products(products.size())
                .userId(user.getId())
                .address(shopAddress)
                .rating(rating)
                .wallet(wallet)
                .followers(subscriptions.size())
                .joinTime(joinTime)
                .phone(phone)
                .shopPackages(shopPackages)
                .numberOfWarning(numberOfWarning)
                .ban(ban)
                .build();
    }
}
