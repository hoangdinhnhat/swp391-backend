package com.swp391.backend.controllers.admin;

import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.counter.Counter;
import com.swp391.backend.model.counter.CounterService;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.notification.NotificationService;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductDTO;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.receiveinfo.ReceiveInfoService;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.report.ReportDTO;
import com.swp391.backend.model.report.ReportService;
import com.swp391.backend.model.saleEvent.SaleEvent;
import com.swp391.backend.model.saleEvent.SaleEventService;
import com.swp391.backend.model.settings.Setting;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.shopPackage.ShopPackageService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
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
    private final NotificationService notificationService;
    private final ReportService reportService;
    private final ProductSaleService productSaleService;
    private final SaleEventService saleEventService;

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
        Setting after = settingService.getById(5);
        var savedSettings = settingService.saveAll(settings);
        Setting before = settingService.getById(5);
        SaleEvent saleEvent = saleEventService.getSaleEventById(1);
        if (before.getValue() != 1)
        {
            var productSales =  productSaleService.getBySaleEvent(saleEvent);
            productSaleService.deleteAll(productSales);
        }else if (before.getValue() == 1 && after.getValue() != 1)
        {
            saleEventService.initSale();
        }
        return ResponseEntity.ok().body(savedSettings);
    }

    @GetMapping("/management/report/product")
    public ResponseEntity<List<ReportDTO>> getProductReports() {
        var reports = reportService.getProductReport()
                .stream()
                .map(Report::toDto)
                .toList();

        return ResponseEntity.ok().body(reports);
    }

    @GetMapping("/management/report/order")
    public ResponseEntity<List<ReportDTO>> getOrderReports() {
        var reports = reportService.getOrderReport()
                .stream()
                .map(Report::toDto)
                .toList();

        return ResponseEntity.ok().body(reports);
    }

    @GetMapping("/management/report/shop")
    public ResponseEntity<List<Report>> getShopReports() {
        var reports = reportService.getShopReport();
        return ResponseEntity.ok().body(reports);
    }

    @PostMapping("/action/product/{id}")
    public ResponseEntity<String> actionProduct(@PathVariable("id") Integer productId, @RequestParam("action") Optional<String> act) {
        String action = act.orElse(null);
        Product product = productService.getProductById(productId);
        if (action == null || product == null) return ResponseEntity.badRequest().build();

        Notification notification = Notification.builder()
                .imageUrl(product.getImages().get(0).getUrl())
                .shop(product.getShop())
                .createdAt(new Date())
                .read(false)
                .build();

        String notiTitle = "";
        String notiContent = "";
        if (action.equals("BAN")) {
            notiTitle = "Product has been banned.";
            notiContent = String.format("Product %s has been banned by Admin. " +
                    "The product will no longer be displayed on the system until the optimal solution is found.", product.getName());

            product.setBan(true);
        } else if (action.equals("RECOVER")) {
            notiTitle = "Your product is allowed to work again.";
            notiContent = String.format("Your shop's %s product has been re-approved by the Admin. " +
                    "Good luck with your shop!", product.getName());

            product.setBan(false);
        }

        productService.save(product);

        notification.setTitle(notiTitle);
        notification.setContent(notiContent);
        notificationService.save(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/action/user/{id}")
    public ResponseEntity<String> actionUser(@PathVariable("id") Integer id, @RequestParam("action") Optional<String> act) {
        String action = act.orElse(null);
        User user = (User) userService.getById(id);
        if (action == null || user == null) return ResponseEntity.badRequest().build();

        Notification notification = Notification.builder()
                .imageUrl(user.getImageurl())
                .user(user)
                .createdAt(new Date())
                .read(false)
                .build();

        if (action.equals("BAN")) {
            notification.setTitle("Admin banned your account");
            notification.setContent("Your account has been banned by the admin. You will not be able to use our services until you find the optimal solution.");

            user.setEnabled(false);
            user.setLogout(true);
        } else if (action.equals("RECOVER")) {
            notification.setTitle("Admin has unlocked your shop");
            notification.setContent("Your account has been unlocked by admin. Wish you happy shopping.");

            user.setEnabled(true);
        }

        userService.save(user);
        notificationService.save(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/action/shop/{id}")
    public ResponseEntity<String> actionShop(
            @PathVariable("id") Integer shopId,
            @RequestParam("action") Optional<String> act
    ) {
        String action = act.orElse(null);
        Shop shop = shopService.getShopById(shopId);
        if (action == null || shop == null) return ResponseEntity.badRequest().build();

        Notification notification = Notification.builder()
                .imageUrl("/api/v1/publics/shop/image/-1")
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();

        if (action.equals("BAN")) {
            notification.setTitle("Admin banned your shop");
            notification.setContent("Your shop has been banned by admin. Now the shop will not be able to post new products until the optimal solution is found.");

            shop.setBan(true);
        } else if (action.equals("RECOVER")) {
            notification.setTitle("Admin has unlocked your shop");
            notification.setContent("Your shop has been allowed to operate again by admin. Now the shop can post new products for sale. Good luck with your shop.");

            shop.setBan(false);
        }

        shopService.save(shop);
        notificationService.save(notification);
        return ResponseEntity.ok().build();
    }

    public void shopProcesser(Shop shop, String action)
    {
        Notification notification = Notification.builder()
                .imageUrl("/api/v1/publics/shop/image/-1")
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();

        if (action.equals("BAN")) {
            notification.setTitle("Admin banned your shop");
            notification.setContent("Your shop has been banned by admin. Now the shop will not be able to post new products until the optimal solution is found.");

            shop.setBan(true);
        } else if (action.equals("WARNING")) {

            notification.setTitle(String.format("%dst warning by admin!", shop.getNumberOfWarning()));
            notification.setContent("Your shop has been reported and admin confirmed the report is correct. Therefore, your shop has been warned for the " + shop.getNumberOfWarning() + "st time. If the number of warnings exceeds 3 times, your shop will be banned from posting new products.");

            int nOw = shop.getNumberOfWarning();
            if (nOw == 3) {
                notification.setTitle("Admin banned your shop");
                notification.setContent("Your shop has been banned by admin. Now the shop will not be able to post new products until the optimal solution is found.");

                shop.setNumberOfWarning(0);
                shop.setBan(true);
            } else {
                shop.setNumberOfWarning(nOw + 1);

                notification.setTitle(String.format("%dst warning by admin!", shop.getNumberOfWarning()));
                notification.setContent("Your shop has been reported and admin confirmed the report is correct. Therefore, your shop has been warned for the " + shop.getNumberOfWarning() + "st time. If the number of warnings exceeds 3 times, your shop will be banned from posting new products.");
            }
        }

        notificationService.save(notification);
        shopService.save(shop);
    }

    @PostMapping("/report/shop/{id}")
    public ResponseEntity<String> reportProcessShop(
            @PathVariable("id") Integer reportId,
            @RequestParam("action") Optional<String> act
    ) {
        String action = act.orElse(null);
        Report report = reportService.getById(reportId);
        if (action == null || report == null) return ResponseEntity.badRequest().build();

        shopProcesser(report.getShop(), action);
        report.setAction("PROCESSED");
        reportService.save(report);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/product/{id}")
    public ResponseEntity<String> reportProcessProduct(
            @PathVariable("id") Integer reportId,
            @RequestParam("action") Optional<String> act
    ) {
        String action = act.orElse(null);
        Report report = reportService.getById(reportId);
        if (action == null || report == null) return ResponseEntity.badRequest().build();

        Product product = report.getProduct();
        Shop shop = product.getShop();
        int nOw = shop.getNumberOfWarning();

        Notification notification = Notification.builder()
                .imageUrl(product.getImages().get(0).getUrl())
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();

        String notiTitle = "";
        String notiContent = "";

        if (action.equals("BAN")) {
            notiTitle = "Product has been banned.";
            notiContent = String.format("Product %s has been banned by Admin. " +
                    "The product will no longer be displayed on the system until the optimal solution is found. ", product.getName());

            product.setBan(true);
            shopProcesser(shop, "WARNING");
            shop.setBan(true);
        } else if (action.equals("WARNING")) {
            notiTitle = "Reported product warning.";
            notiContent = String.format("Product %s has been reported and admin has sent a warning to your shop. " +
                    "Shop please solve it quickly if you don't want to be banned. ", product.getName());
            shopProcesser(shop, "WARNING");
        }

        notification.setTitle(notiTitle);
        notification.setContent(notiContent);
        notificationService.save(notification);

        report.setAction("PROCESSED");
        reportService.save(report);

        shopService.save(shop);
        productService.save(product);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/report/order/{id}")
    public ResponseEntity<String> reportProcessOrder(
            @PathVariable("id") Integer reportId,
            @RequestParam("action") Optional<String> act
    ) {
        String action = act.orElse(null);
        Report report = reportService.getById(reportId);
        if (action == null || report == null) return ResponseEntity.badRequest().build();

        User user = report.getReporter();
        Order order = report.getOrder();

        Notification notification = Notification.builder()
                .imageUrl(user.getImageurl())
                .user(user)
                .createdAt(new Date())
                .read(false)
                .build();

        String notiTitle = "";
        String notiContent = "";

        if (action.equals("WARNING")) {
            notiTitle = "Warning for fraud.";
            notiContent = String.format("We have confirmed that you have indeed successfully received your order %s but still report not received. We send you the first warning.", report.getOrder().getId());

        } else if (action.equals("CONFIRM")) {
            notiTitle = "Confirmed that the order has not been received even though it has been delivered.";
            notiContent = String.format("Sorry for these inconveniences. We have confirmed that there is a problem on the shipping side about the %s order. We have refunded you. Again apologize to you.", report.getOrder().getId());
            if ("ZaloPay Wallet".equals(order.getPayment()))
            {
                Counter wallet = counterService.getById("WALLET");
                user.setWallet(user.getWallet() + order.getSoldPrice() + order.getShippingFee());
                wallet.setValue(wallet.getValue() - order.getSoldPrice() - order.getShippingFee());
                userService.save(user);
                counterService.save(wallet);
            }
        }

        notification.setTitle(notiTitle);
        notification.setContent(notiContent);
        notificationService.save(notification);

        report.setAction("PROCESSED");
        reportService.save(report);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/wallet/")
    public ResponseEntity<Double> getWallet() {
        var wallet = counterService.getById("WALLET");
        return ResponseEntity.ok().body((Double) wallet.getValue());
    }
}
