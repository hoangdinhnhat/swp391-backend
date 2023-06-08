package com.swp391.backend.model.productSale;

import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.saleEvent.SaleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSaleService {
    private final ProductSaleRepository productSaleRepository;

    public ProductSale save(ProductSale productSale) {
        return productSaleRepository.save(productSale);
    }

    public List<ProductSale> getALlProductSale() {
        return productSaleRepository.findAll();
    }

    public List<ProductSale> getBySaleEvent(SaleEvent saleEvent, Integer page) {
        Pageable pageable = PageRequest.of(page, 20);
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
