package com.swp391.backend.model.productFeedback;

import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductRepository;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final SettingService settingService;

    public double roundedFloat(double num)
    {
        return (double) Math.round(num * 100) / 100;
    }

    @Transactional
    public Feedback save(Feedback feedback) {
        Feedback feedbackReturn = feedbackRepository.saveAndFlush(feedback);
        Product product = productRepository.findById(feedback.getProduct().getId()).orElse(null);
        product.setRating(roundedFloat(product.getRating()));
        productRepository.saveAndFlush(product);

        Shop shop = shopRepository.findById(product.getShop().getId()).orElse(null);
        shop.setRating(roundedFloat(shop.getRating()));
        shopRepository.save(shop);
        return feedbackReturn;
    }

    public Feedback saveSpecial(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void delete(Integer id) {
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> getByProduct(Product product, Integer page) {
        Pageable pageable = PageRequest.of(page, settingService.getById(4).getValue(), Sort.by("time").descending());
        return feedbackRepository.findByProduct(product, pageable);
    }

    public List<Feedback> getByProductAndRate(Product product, Integer rate, Integer page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("time").descending());
        return feedbackRepository.findByProductAndRate(product, rate, pageable);
    }

    public int ceil(float num)
    {
        int rounded = Math.round(num);
        if (rounded < num)
        {
            rounded += 1;
        }
        return rounded;
    }

    public Integer getMaxFeedback(Shop shop, Integer rate) {
        var feedbacks =  feedbackRepository.findByShopAndRate(shop, rate);
        return feedbacks.size();
    }

    public Integer getMaxFeedback(Shop shop) {
        var feedbacks =  feedbackRepository.findByShop(shop);
        return feedbacks.size();
    }

    public List<Feedback> getByShop(Shop shop, Integer page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("time").descending());
        return feedbackRepository.findByShopAndProcessed(shop, false, pageable);
    }

    public List<Feedback> getByShopAndRate(Shop shop, Integer rate, Integer page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("time").descending());
        return feedbackRepository.findByShopAndRateAndProcessed(shop, rate, false, pageable);
    }

    public List<Integer> getNumberOfFeedbackAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByHourRange(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInPreviousDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByHourRangePreviousDay(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByDayRange(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInPreviousWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 2;
        for (int i = 7 * weekRange + 1; i <= 7 * (weekRange + 1); i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByDayRange(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByDayRange(shop.getId(), prev, i * 7);
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInPreviousMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByDayRangePreviousMonth(shop.getId(), prev, i * 7);
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByMonthRange(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfFeedbackAnalystInPreviousYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = feedbackRepository.getFeedbackAnalystByMonthRangePreviousYear(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public Feedback getById(Integer id) {
        return feedbackRepository.findById(id).orElse(null);
    }
}
