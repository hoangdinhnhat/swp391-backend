package com.swp391.backend.controllers.admin;

import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.counter.CounterService;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.product.ProductDTO;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.receiveinfo.ReceiveInfoService;
import com.swp391.backend.model.settings.Setting;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.shopPackage.ShopPackageService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final ShopService shopService;
    private final OrderService orderService;
    private final ReceiveInfoService receiveInfoService;
    private final SettingService settingService;
    private final CategoryService categoryService;
    private final ShopPackageService shopPackageService;
    private final CounterService counterService;

    @GetMapping("/analyst/total")
    public ResponseEntity<List<Integer>> getTotalOrders() {
        String totalVisits = Math.round(counterService.getById("VISIT_PAGE").getValue()) + "";
        Integer totalVi = Integer.parseInt(totalVisits);
        Integer totalOrders = orderService.getTotalOrder();
        Integer totalProducts = productService.getTotalProduct();
        Integer totalNewlyUsers = userService.getNewUserInMonth();

        List<Integer> results = new ArrayList<>();
        results.add(totalOrders);
        results.add(totalProducts);
        results.add(totalVi);
        results.add(totalNewlyUsers);

        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/analyst/category/product")
    public ResponseEntity<List<Integer>> getProductInCategory() {
        List<Integer> categoryQuantity = categoryService.getNumberOfCategoryAnalyst();
        return ResponseEntity.ok().body(categoryQuantity);
    }

    @GetMapping("/analyst/category/revenue")
    public ResponseEntity<List<List<Integer>>> getRevenueOfCategory() {
        List<List<Integer>> categoryQuantity = categoryService.getRevenueOfCategoryLast14dayAnalyst();
        return ResponseEntity.ok().body(categoryQuantity);
    }

    @GetMapping("/analyst/weekly-revenue")
    public ResponseEntity<List<Double>> getWeeklyRevenue() {
        var weeklyRevenue = shopPackageService.getWeeklyRevenue();
        return ResponseEntity.ok().body(weeklyRevenue);
    }

    @GetMapping("/management/user")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getAllUser();
        List<UserDTO> userDtos = users.stream()
                .map(u -> {
                    var defaultInfo = receiveInfoService.getDefaultReceiveInfo(u);
                    var dto = u.toDto();
                    dto.setDefaultReceiveInfo(defaultInfo);
                    return dto;
                })
                .toList();
        return ResponseEntity.ok().body(userDtos);
    }

    @GetMapping("/management/shop")
    public ResponseEntity<List<ShopDTO>> getShops() {
        var shops = shopService.getAllShop();
        var shopDtos = shops.stream()
                .map(s -> s.toDto())
                .toList();
        return ResponseEntity.ok().body(shopDtos);
    }

    @GetMapping("/management/product")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        var products = productService.getAllProductForAdmin();
        List<ProductDTO> productDTOS = products.stream()
                .map(p -> {
                    return ProductDTO.builder()
                            .id(p.getId())
                            .name(p.getName())
                            .images(p.getImages())
                            .category(p.getCategoryGroup().getCategory().toDto())
                            .shop(p.getShop().toDto())
                            .ban(p.isBan())
                            .build();
                })
                .toList();
        return ResponseEntity.ok().body(productDTOS);
    }

    @GetMapping("/management/setting")
    public ResponseEntity<List<Setting>> getSettings() {
        var settings = settingService.getAll();
        return ResponseEntity.ok().body(settings);
    }

    @PostMapping("/management/setting/update")
    public ResponseEntity<List<Setting>> updateSettings(@RequestBody List<Setting> settings) {
        var savedSettings = settingService.saveAll(settings);
        return ResponseEntity.ok().body(savedSettings);
    }

    @GetMapping("/wallet/")
    public ResponseEntity<Double> getWallet() {
        var wallet = counterService.getById("WALLET");
        return ResponseEntity.ok().body((Double) wallet.getValue());
    }
}
