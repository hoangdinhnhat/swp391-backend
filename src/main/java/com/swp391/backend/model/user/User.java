/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.ConversationChatter.ConversationChatter;
import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.subscription.Subscription;
import com.swp391.backend.model.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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
@EqualsAndHashCode
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<Subscription> subscriptions;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    List<Notification> notifications;
    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String imageurl;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean isLogout = true;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Date timeout;
    private Date joinAt;
    private Boolean locked = false;
    private Boolean enabled = false;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Collection<Token> tokens;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Collection<ReceiveInfo> receiveinfos;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Collection<Feedback> feedbacks;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonManagedReference
    private Cart cart;
    private int wrongpasswordcounter = 0;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Shop> shops;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<ConversationChatter> conversationChatters;

    @OneToMany(mappedBy = "reporter")
    @JsonBackReference
    private List<Report> reports;
    private double wallet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public UserDTO toDto() {
        return UserDTO
                .builder()
                .id(id)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .enabled(enabled)
                .wallet(wallet)
                .imageurl(imageurl)
                .gender(gender)
                .build();
    }
}
