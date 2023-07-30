package com.swp391.backend.model.categoryGroup;

import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryService categoryService;
    private final SettingService settingService;

    public CategoryGroup getCategoryGroupById(Integer id) {
        return categoryGroupRepository.findById(id).orElse(null);
    }

    public CategoryGroup save(CategoryGroup categoryGroup) {
        return categoryGroupRepository.save(categoryGroup);
    }

    public void delete(Integer id) {
        categoryGroupRepository.deleteById(id);
    }

    public List<Product> getProductByCategoryGroups(Product product, List<CategoryGroup> categoryGroups, Integer num) {
        List<Product> products = new ArrayList<>();
        categoryGroups.forEach(c -> {
            c.getProducts().forEach(it -> {
                if (products.size() < num) {
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

    public List<CategoryGroupSold> getTopThreeSoldCategoryGroupInDay(Shop shop) {
        List<CategoryGroupSold> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        List<CategoryGroup> categoryGroups = categoryGroupRepository.getTopThreeSoldCategoryGroupByHourRange(shop.getId(), hourRange);
        categoryGroups.forEach(it -> {
            Integer sold = categoryGroupRepository.getSoldByCategoryGroupByHourRange(shop.getId(), it.getId(), hourRange);
            CategoryGroupSold categoryGroupSold = CategoryGroupSold.builder()
                    .categoryGroup(it.toDTO())
                    .sold(sold)
                    .build();

            rs.add(categoryGroupSold);
        });

        return rs;
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
                .name("Bird General")
                .category(categoryService.getById(1))
                .build();

        CategoryGroup group4 = CategoryGroup.builder()
                .name("Red-whiskered bulbul's food")
                .category(categoryService.getById(2))
                .build();

        CategoryGroup group5 = CategoryGroup.builder()
                .name("Sparrow's food")
                .category(categoryService.getById(2))
                .build();

        CategoryGroup group6 = CategoryGroup.builder()
                .name("Bird's food general")
                .category(categoryService.getById(2))
                .build();

        CategoryGroup group7 = CategoryGroup.builder()
                .name("Red-whiskered bulbul's cage")
                .category(categoryService.getById(3))
                .build();

        CategoryGroup group8 = CategoryGroup.builder()
                .name("Sparrow's cage")
                .category(categoryService.getById(3))
                .build();

        CategoryGroup group9 = CategoryGroup.builder()
                .name("Bird's cage general")
                .category(categoryService.getById(3))
                .build();

        CategoryGroup group10 = CategoryGroup.builder()
                .name("Inox Bird's cage")
                .category(categoryService.getById(3))
                .build();

        CategoryGroup group11 = CategoryGroup.builder()
                .name("Red-whiskered bulbul's accessory")
                .category(categoryService.getById(4))
                .build();

        CategoryGroup group12 = CategoryGroup.builder()
                .name("Sparrow's accessory")
                .category(categoryService.getById(4))
                .build();

        CategoryGroup group13 = CategoryGroup.builder()
                .name("Bird's accessory general")
                .category(categoryService.getById(4))
                .build();

        save(group1);
        save(group2);
        save(group3);
        save(group4);
        save(group5);
        save(group6);
        save(group7);
        save(group8);
        save(group9);
        save(group10);
        save(group11);
        save(group12);
        save(group13);
    }
}
