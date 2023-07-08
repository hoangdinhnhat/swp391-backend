package com.swp391.backend.model.shop;

import com.swp391.backend.model.shopAddress.ShopAddress;
import com.swp391.backend.model.shopPackage.ShopPackage;
import lombok.*;

import java.util.Date;
import java.util.List;

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
    private Integer userId;
    private Date joinTime;
    private int followers;
    private int products;
    private boolean ban;
    private int numberOfWarning;
    private ShopAddress address;
    private double wallet;
    private String phone;
    private List<ShopPackage> shopPackages;
}
