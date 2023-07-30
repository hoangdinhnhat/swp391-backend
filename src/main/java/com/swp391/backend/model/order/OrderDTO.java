package com.swp391.backend.model.order;

import com.swp391.backend.model.orderDetails.OrderDetailsDTO;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDTO {
    private String id;
    private ShopDTO shop;
    private User user;
    private ReceiveInfo receiveInfo;
    private List<OrderDetailsDTO> orderDetails;
    private Date createdTime;
    private double shippingFee;
    private String payment;
    private String description;
    private double sellPrice;
    private double soldPrice;
    private String status;
    private boolean special;
    private String expectedReceive;
    private List<Report> reports;
    private boolean reported;
}
