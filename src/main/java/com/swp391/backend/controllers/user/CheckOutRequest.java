package com.swp391.backend.controllers.user;

import com.swp391.backend.model.shop.ShopDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CheckOutRequest {
    private Integer shopId;
    private List<CheckOutItem> checkOutItems;
    private Integer shippingFee;
}
