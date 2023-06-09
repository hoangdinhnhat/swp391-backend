package com.swp391.backend.model.categoryGroup;

import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.product.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryService categoryService;

    public List<Product> getProductByCategoryGroups(Product product, List<CategoryGroup> categoryGroups) {
        List<Product> products = new ArrayList<>();
        categoryGroups.forEach(c -> {
            c.getProducts().forEach(it -> {
                if (products.size() < 6) {
                    products.add(it);
                }
            });
        });

        products.sort((a, b) -> {
            int distanceA = Math.abs(product.getShop().getId() - a.getShop().getId());
            int distanceB = Math.abs(product.getShop().getId() - b.getShop().getId());

            return distanceA - distanceB;
        });
        return products;
    }

    public CategoryGroup getCategoryGroupById(Integer id) {
        return categoryGroupRepository.findById(id).orElse(null);
    }

    public CategoryGroup save(CategoryGroup categoryGroup) {
        return categoryGroupRepository.save(categoryGroup);
    }

    public void delete(Integer id) {
        categoryGroupRepository.deleteById(id);
    }

    public void init() {
        CategoryGroup group1 = CategoryGroup.builder()
                .name("Red-whiskered bulbul")
                .category(categoryService.getById(1))
                .build();

        CategoryGroup group2 = CategoryGroup.builder()
                .name("Sparrow")
                .category(categoryService.getById(1))
                .build();

        CategoryGroup group3 = CategoryGroup.builder()
                .name("Sparrow's food")
                .category(categoryService.getById(2))
                .build();

        CategoryGroup group4 = CategoryGroup.builder()
                .name("Sparrow's cage")
                .category(categoryService.getById(3))
                .build();

        CategoryGroup group5 = CategoryGroup.builder()
                .name("Bird's cage")
                .category(categoryService.getById(3))
                .build();

        CategoryGroup group6 = CategoryGroup.builder()
                .name("Inox Bird's cage")
                .category(categoryService.getById(3))
                .build();

        save(group1);
        save(group2);
        save(group3);
        save(group4);
        save(group5);
        save(group6);
    }
}
