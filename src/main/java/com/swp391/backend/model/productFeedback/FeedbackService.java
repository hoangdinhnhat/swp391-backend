package com.swp391.backend.model.productFeedback;

import com.swp391.backend.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public Feedback save(Feedback feedback)
    {
        return feedbackRepository.save(feedback);
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
