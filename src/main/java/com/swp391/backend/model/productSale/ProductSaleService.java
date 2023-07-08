package com.swp391.backend.model.productSale;

import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.saleEvent.SaleEvent;
import com.swp391.backend.model.settings.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSaleService {
    private final ProductSaleRepository productSaleRepository;
    private final SettingService settingService;

    public ProductSale save(ProductSale productSale) {
        return productSaleRepository.save(productSale);
    }

    public List<ProductSale> getALlProductSale() {
        return productSaleRepository.findAll();
    }

    public List<ProductSale> getBySaleEvent(SaleEvent saleEvent, Integer page) {
        Integer size = settingService.getById(1).getValue();
        Pageable pageable = PageRequest.of(page, size);
        return productSaleRepository.findBySaleEvent(saleEvent, pageable);
    }

    public ProductSale findProductInSale(Product product) {
        ProductSale finded = null;

        for (ProductSale ps : getALlProductSale()) {
            finded = productSaleRepository.findBySaleEvent(ps.getSaleEvent()).stream()
                    .filter(it -> it.getProduct().getId() == product.getId())
                    .findFirst().orElse(null);
        }

        return finded;
    }

    public void delete(ProductSaleKey id) {
        productSaleRepository.deleteById(id);
    }
}
