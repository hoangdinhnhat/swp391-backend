package com.swp391.backend.model.shopAddress;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopAddressService {

    private final ShopAddressRepository shopAddressRepository;

    public ShopAddress save(ShopAddress shopAddress) {
        return shopAddressRepository.save(shopAddress);
    }
}
