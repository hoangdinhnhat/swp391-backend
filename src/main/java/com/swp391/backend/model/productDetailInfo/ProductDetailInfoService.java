package com.swp391.backend.model.productDetailInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDetailInfoService {
    private final ProductDetailInfoRepository productDetailInfoRepository;

    public ProductDetailInfo save(ProductDetailInfo productDetailInfo) {
        return productDetailInfoRepository.save(productDetailInfo);
    }
}
