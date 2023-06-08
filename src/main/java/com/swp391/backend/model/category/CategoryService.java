package com.swp391.backend.model.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category category)
    {
        return categoryRepository.save(category);
    }

    public Category getById(Integer id)
    {
        return categoryRepository.findById(id).orElse(null);
    }

    public void init()
    {
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
