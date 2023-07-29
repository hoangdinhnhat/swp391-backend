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

    public void deleteAll(List<ProductSale> productSales)
    {
        productSaleRepository.deleteAll(productSales);
    }

    public List<ProductSale> getALlProductSale() {
        return productSaleRepository.findAll();
    }

    public List<ProductSale> getBySaleEvent(SaleEvent saleEvent) {
        return productSaleRepository.findBySaleEvent(saleEvent);
    }

    public List<ProductSale> getBySaleEvent(SaleEvent saleEvent, Integer page) {
        Integer size = settingService.getById(1).getValue();
        Pageable pageable = PageRequest.of(page, size);
        return productSaleRepository.findBySaleEvent(saleEvent, pageable);
    }

    public ProductSale findProductInSale(Product product) {
        if (product.getProductSales().size() > 0)
        {
            return product.getProductSales().get(0);
        }
        return null;
    }

    public void delete(ProductSaleKey id) {
        productSaleRepository.deleteById(id);
    }
}
