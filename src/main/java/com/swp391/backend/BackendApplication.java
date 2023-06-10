package com.swp391.backend;

import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.productAttachWith.AttachWithService;
import com.swp391.backend.model.saleEvent.SaleEventService;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.utils.storage.ProductVideoService;
import com.swp391.backend.utils.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductVideoService storageService) {
        return (args) -> {
//            storageService.deleteAll();
            storageService.init();
        };
    }

    @Bean
    CommandLineRunner initUser(UserService userService) {
        return (args) -> {
            userService.init();
        };
    }

    @Bean
    CommandLineRunner init1(ShopService shopService) {
        return (args) -> {
            shopService.init();
        };
    }

    @Bean
    CommandLineRunner init2(CategoryService categoryService) {
        return (args) -> {
            categoryService.init();
        };
    }

    @Bean
    CommandLineRunner init3(CategoryDetailInfoService categoryDetailInfoService) {
        return (args) -> {
            categoryDetailInfoService.init();
        };
    }

    @Bean
    CommandLineRunner init4(CategoryGroupService categoryGroupService) {
        return (args) -> {
            categoryGroupService.init();
        };
    }

    @Bean
    CommandLineRunner init5(AttachWithService attachWithService) {
        return (args) -> {
            attachWithService.init();
        };
    }

    @Bean
    CommandLineRunner init6(ProductService productService) {
        return (args) -> {
            productService.init();
        };
    }

    @Bean
    CommandLineRunner init7(SaleEventService saleEventService) {
        return (args) -> {
            saleEventService.init();
        };
    }
}
