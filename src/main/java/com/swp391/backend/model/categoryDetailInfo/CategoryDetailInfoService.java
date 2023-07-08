package com.swp391.backend.model.categoryDetailInfo;

import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryDetailInfoService {
    private final CategoryDetailInfoRepository categoryDetailInfoRepository;
    private final CategoryService categoryService;

    public List<CategoryDetailInfo> getByCategory(Category category) {
        return categoryDetailInfoRepository.findByCategory(category);
    }

    public CategoryDetailInfo save(CategoryDetailInfo categoryDetailInfo) {
        return categoryDetailInfoRepository.save(categoryDetailInfo);
    }

    public void init() {
        CategoryDetailInfo d1 = CategoryDetailInfo.builder()
                .name("Age")
                .category(categoryService.getById(1))
                .build();

        CategoryDetailInfo d2 = CategoryDetailInfo.builder()
                .name("Weight")
                .category(categoryService.getById(1))
                .build();

        CategoryDetailInfo d3 = CategoryDetailInfo.builder()
                .name("Brand")
                .category(categoryService.getById(3))
                .build();

        CategoryDetailInfo d4 = CategoryDetailInfo.builder()
                .name("Brand")
                .category(categoryService.getById(2))
                .build();

        CategoryDetailInfo d5 = CategoryDetailInfo.builder()
                .name("Material")
                .category(categoryService.getById(3))
                .build();

        CategoryDetailInfo d6 = CategoryDetailInfo.builder()
                .name("Material")
                .category(categoryService.getById(2))
                .build();

        save(d1);
        save(d2);
        save(d3);
        save(d4);
        save(d5);
        save(d6);
    }
}
