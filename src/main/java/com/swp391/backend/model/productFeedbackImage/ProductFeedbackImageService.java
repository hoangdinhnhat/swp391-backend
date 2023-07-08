package com.swp391.backend.model.productFeedbackImage;

import com.swp391.backend.model.productFeedback.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFeedbackImageService {
    private final ProductFeedbackImageRepository productFeedbackImageRepository;

    public ProductFeedbackImage save(ProductFeedbackImage productFeedbackImage) {
        return productFeedbackImageRepository.save(productFeedbackImage);
    }

    public void delete(Integer id) {
        productFeedbackImageRepository.deleteById(id);
    }

    public List<ProductFeedbackImage> getByFeedback(Feedback feedback) {
        return productFeedbackImageRepository.findByFeedback(feedback);
    }
}
