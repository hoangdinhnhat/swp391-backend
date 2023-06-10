package com.swp391.backend.model.productFeedback;

import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductRepository;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;


    @Transactional
    public Feedback save(Feedback feedback)
    {
        Feedback feedbackReturn = feedbackRepository.saveAndFlush(feedback);
        Product product = productRepository.findById(feedback.getProduct().getId()).orElse(null);
        product.setRating(product.getRating());
        productRepository.saveAndFlush(product);

        Shop shop = shopRepository.findById(product.getShop().getId()).orElse(null);
        shop.setRating(shop.getRating());
        shopRepository.save(shop);
        return feedbackReturn;
    }

    public void delete(Integer id)
    {
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> getByProduct(Product product, Integer page)
    {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("time").descending());
        return feedbackRepository.findByProduct(product, pageable);
    }

    public List<Feedback> getByProductAndRate(Product product, Integer rate, Integer page)
    {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("time").descending());
        return feedbackRepository.findByProductAndRate(product, rate, pageable);
    }

    public Feedback getById(Integer id)
    {
        return feedbackRepository.findById(id).orElse(null);
    }
}
