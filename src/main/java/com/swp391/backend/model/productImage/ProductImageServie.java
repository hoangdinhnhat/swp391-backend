package com.swp391.backend.model.productImage;

import com.swp391.backend.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServie {
    private final ProductImageRepository productImageRepository;

    public ProductImage save(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    public void delete(Integer id) {
        productImageRepository.deleteById(id);
    }

    public void deleteAll(List<ProductImage> productImages)
    {
        productImageRepository.deleteAll(productImages);
    }

    public List<ProductImage> getByProduct(Product product) {
        return productImageRepository.findByProduct(product);
    }
}
