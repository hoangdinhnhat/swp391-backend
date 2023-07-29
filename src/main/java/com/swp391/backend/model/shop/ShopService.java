package com.swp391.backend.model.shop;

import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.district.District;
import com.swp391.backend.model.district.DistrictService;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.province.ProvinceService;
import com.swp391.backend.model.shopAddress.ShopAddress;
import com.swp391.backend.model.shopAddress.ShopAddressService;
import com.swp391.backend.model.shopPackage.ShopPackage;
import com.swp391.backend.model.shopPackage.ShopPackageService;
import com.swp391.backend.model.shopPlan.ShopPlan;
import com.swp391.backend.model.shopPlan.ShopPlanService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.model.ward.Ward;
import com.swp391.backend.model.ward.WardService;
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
    private final ShopAddressService shopAddressService;
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;
    private final UserService userService;
    private final ShopPackageService shopPackageService;
    private final ShopPlanService shopPlanService;

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    public void delete(Integer id) {
        shopRepository.deleteById(id);
    }

    public Shop getShopById(Integer id) {
        return shopRepository.findById(id).orElse(null);
    }

    public List<Shop> getAllShop() {
        Pageable pageable = PageRequest.of(0, 666, Sort.by("joinTime").descending());
        return shopRepository.findAll(pageable).getContent();
    }

    public List<Shop> getShopTrending() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("rating").descending());
        var page = shopRepository.findAll(pageable);
        return page.getContent();
    }

    public Shop searchShop(String search) {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("rating").descending());
        List<Shop> finded = shopRepository.findByNameContainingIgnoreCase(search, pageable);
        return finded.size() > 0 ? finded.get(0) : null;
    }

    public List<Shop> topThreeShopInCategory(Category category) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("rating").descending());
        return shopRepository.findByCategory(category, pageable);
    }

    public void init() {

        User user = (User) userService.loadUserByUsername("nhathdse160377@fpt.edu.vn");
        User user2 = (User) userService.loadUserByUsername("vuducthien@gmail.com");
        User user3 = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");

        ShopPlan unregiste = shopPlanService.getByPlan("UNREGISTE");
        ShopPlan trial = shopPlanService.getByPlan("FREE TRIAL");

        Province province = Province.builder()
                .id(201)
                .name("Hà Nội")
                .build();
        provinceService.save(province);

        District district = District.builder()
                .id(1489)
                .name("Quận Hoàn Kiếm")
                .build();
        districtService.save(district);

        Ward ward = Ward.builder()
                .id("1A0218")
                .name("Phường Tràng Tiền")
                .build();
        wardService.save(ward);

        String specificAddress = "Số 302";

        ShopAddress shopAddress = ShopAddress.builder()
                .province(province)
                .district(district)
                .ward(ward)
                .specificAddress(specificAddress)
                .build();
        shopAddressService.save(shopAddress);

        Shop shop = Shop.builder()
                .name("Adidas's Bird")
                .user(user2)
                .shopImage("/api/v1/publics/shop/image/1")
                .shopAddress(shopAddress)
                .phone("0914536782")
                .joinTime(new Date())
                .build();
        save(shop);
        ShopPackage shopPackage = ShopPackage.builder()
                .shopPlan(trial)
                .shop(shop)
                .build();
        shopPackageService.save(shopPackage);

        shop = Shop.builder()
                .name("Nike's Bird Food")
                .user(user)
                .shopImage("/api/v1/publics/shop/image/2")
                .shopAddress(shopAddress)
                .phone("0967892356")
                .joinTime(new Date())
                .build();
        save(shop);
        shopPackage = ShopPackage.builder()
                .shopPlan(unregiste)
                .shop(shop)
                .build();
        shopPackageService.save(shopPackage);

        shop = Shop.builder()
                .name("Louis Vuitton's Bird Cage And Bird Accessories")
                .user(user3)
                .shopImage("/api/v1/publics/shop/image/3")
                .shopAddress(shopAddress)
                .phone("0956782453")
                .joinTime(new Date())
                .build();
        save(shop);
        shopPackage = ShopPackage.builder()
                .shopPlan(trial)
                .shop(shop)
                .build();
        shopPackageService.save(shopPackage);
    }
}
