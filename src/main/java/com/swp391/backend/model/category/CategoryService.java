package com.swp391.backend.model.category;

import com.swp391.backend.model.shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Integer> getNumberOfCategoryAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        List<Integer> categories = categoryRepository.getCategoryInShop(shop.getId());

        categories.forEach(it -> {
            Integer num = categoryRepository.getCategoryAnalystByHourRange(shop.getId(), it, hourRange);
            if (num == null) num = 0;
            rs.add(num);
        });

        return rs;
    }

    public List<Integer> getRevenueOfCategoryAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        List<Integer> categories = categoryRepository.getCategoryInShop(shop.getId());

        categories.forEach(it -> {
            Integer num = categoryRepository.getRevenueByCategoryAndHourRange(shop.getId(), it, hourRange);
            if (num == null) num = 0;
            rs.add(num);
        });

        return rs;
    }

    public List<Integer> getRevenueOfCategoryAnalyst(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        List<Integer> categories = categoryRepository.getCategoryInShop(shop.getId());

        categories.forEach(it -> {
            Integer num = categoryRepository.getRevenueByCategory(shop.getId(), it);
            if (num == null) num = 0;
            rs.add(num);
        });

        return rs;
    }

    public List<List<Integer>> getRevenueOfCategoryLast14dayAnalyst() {
        List<List<Integer>> rs = new ArrayList<>();
        List<Integer> categories = categoryRepository.getCategory();

        categories.forEach(it -> {
            List<Integer> reven = new ArrayList<>();
            for (int i = 13; i >= 0; i--) {
                Integer num = categoryRepository.getRevenueByCategoryLast14Day(i, it);
                if (num == null) num = 0;
                reven.add(num);
            }
            rs.add(reven);
        });

        return rs;
    }

    public List<Integer> getNumberOfCategoryAnalyst(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        List<Integer> categories = categoryRepository.getCategoryInShop(shop.getId());

        categories.forEach(it -> {
            Integer num = categoryRepository.getNumberOfCategory(shop.getId(), it);
            if (num == null) num = 0;
            rs.add(num);
        });

        return rs;
    }

    public List<Integer> getNumberOfCategoryAnalyst() {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        List<Integer> categories = categoryRepository.getCategory();

        categories.forEach(it -> {
            Integer num = categoryRepository.getNumberOfCategory(it);
            if (num == null) num = 0;
            rs.add(num);
        });

        return rs;
    }

    public List<Category> getCategoryInShop(Shop shop) {
        return categoryRepository.getCategoryInShop(shop.getId())
                .stream()
                .map(it -> getById(it))
                .collect(Collectors.toList());
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public void init() {
        Category bird = Category.builder()
                .name("Bird")
                .build();

        Category birdCage = Category.builder()
                .name("Bird Cage")
                .build();

        Category birdFood = Category.builder()
                .name("Bird Food")
                .build();

        Category birdAccessory = Category.builder()
                .name("Bird Accessory")
                .build();

        save(bird);
        save(birdFood);
        save(birdCage);
        save(birdAccessory);
    }
}
