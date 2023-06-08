package com.swp391.backend.model.shop;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ShopDTO {
    private Integer id;
    private String name;
    private String shopImage;
    private double rating;
    private Date joinTime;
    private int followers;
    private int products;
}
