package com.swp391.backend.model.productFeedback;

import com.swp391.backend.model.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FeedbackRepository extends PagingAndSortingRepository<Feedback, Integer>, JpaRepository<Feedback, Integer> {
    List<Feedback> findByProduct(Product product, Pageable pageable);
    List<Feedback> findByProductAndRate(Product product, Integer rate, Pageable pageable);
}
