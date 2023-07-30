package com.swp391.backend.model.report;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swp391.backend.model.order.OrderDTO;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductDTO;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportDTO {
    private Integer id;

    private UserDTO reporter;

    private Shop shop;

    private ProductDTO product;
    private OrderDTO order;

    private String reasonType;
    private String reasonSpecific;
}
