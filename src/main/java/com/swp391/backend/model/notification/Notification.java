package com.swp391.backend.model.notification;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    private Shop shop;

    private String title;
    private String content;
    private String imageUrl;
    private String redirectUrl;

    private Integer typeId;
    private Date createdAt;
    private boolean read;
}
