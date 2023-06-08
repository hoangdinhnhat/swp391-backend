package com.swp391.backend.model.productFeedbackImage;

import com.swp391.backend.model.productFeedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFeedbackImageRepository extends JpaRepository<ProductFeedbackImage, Integer> {
    List<ProductFeedbackImage> findByFeedback(Feedback feedback);
}
