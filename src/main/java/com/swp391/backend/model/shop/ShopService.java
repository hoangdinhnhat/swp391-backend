package com.swp391.backend.model.shop;

import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.shopAddress.ShopAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    public void delete(Integer id) {
        shopRepository.deleteById(id);
    }

    public Shop getShopById(Integer id) {
        return shopRepository.findById(id).orElse(null);
    }

    public List<Shop> getShopTrending()
    {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("rating").descending());
        var page = shopRepository.findAll(pageable);
        return page.getContent();
    }

    public Shop searchShop(String search) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("rating").descending());
        List<Shop> finded = shopRepository.findByNameContainingIgnoreCase(search, pageable);
        return finded.size() > 0 ? finded.get(0) : null;
    }

    public List<Shop> topThreeShopInCategory(Category category)
    {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("rating").descending());
        return shopRepository.findByCategory(category.getId(), pageable);
    }

    public void init() {
        Shop shop = Shop.builder()
                .name("Adidas's Bird")
                .shopImage("/api/v1/publics/shop/image/1")
                .joinTime(new Date())
                .build();
        save(shop);

        shop = Shop.builder()
                .name("Nike's Bird Food")
                .shopImage("/api/v1/publics/shop/image/2")
                .joinTime(new Date())
                .build();
        save(shop);

        shop = Shop.builder()
                .name("Louis Vuitton's Bird Cage And Bird Accessories")
                .shopImage("/api/v1/publics/shop/image/3")
                .joinTime(new Date())
                .build();
        save(shop);
    }
}
