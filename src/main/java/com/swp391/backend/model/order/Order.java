package com.swp391.backend.model.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_order")
public class Order {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonBackReference
    private Shop shop;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "receive_info_id")
    private ReceiveInfo receiveInfo;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<OrderDetails> orderDetails;

    @OneToMany(mappedBy = "order")
    @JsonBackReference
    private List<Report> reports;
    private boolean reported;

    private Date createdTime;
    private double shippingFee;
    private String payment;
    private String description;
    private double sellPrice;
    private double soldPrice;
    private boolean special;
    private String expectedReceive;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public OrderDTO toDto() {
        var orderDetailsDTOs = orderDetails.stream()
                .map(it -> it.toDto())
                .toList();

        return OrderDTO.builder()
                .id(id)
                .shop(shop.toDto())
                .user(user)
                .receiveInfo(receiveInfo)
                .orderDetails(orderDetailsDTOs)
                .createdTime(createdTime)
                .shippingFee(shippingFee)
                .payment(payment)
                .description(description)
                .expectedReceive(expectedReceive)
                .sellPrice(sellPrice)
                .soldPrice(soldPrice)
                .status(status.name())
                .special(special)
                .reported(reported)
                .reports(reports)
                .build();
    }
}
