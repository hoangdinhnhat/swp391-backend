package com.swp391.backend.controllers.shop;

import com.google.gson.Gson;
import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.controllers.publics.ProductRequest;
import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.category.CategoryDTO;
import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfo;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.district.District;
import com.swp391.backend.model.district.DistrictService;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.notification.NotificationService;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.order.OrderStatus;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfoService;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackDTO;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productImage.ProductImageServie;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.province.ProvinceService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.shopAddress.ShopAddress;
import com.swp391.backend.model.shopAddress.ShopAddressService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.ward.Ward;
import com.swp391.backend.model.ward.WardService;
import com.swp391.backend.utils.storage.ProductImageStorageService;
import com.swp391.backend.utils.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

    @Autowired
    private Gson gsonUtils;

    @Autowired
    @Qualifier(value = "productImage")
    private StorageService productImageStorageService;

    @Autowired
    @Qualifier(value = "productVideo")
    private StorageService productVideoStorageService;

    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;
    private final ShopAddressService shopAddressService;
    private final ShopService shopService;
    private final AuthenticatedManager authenticatedManager;
    private final ProductService productService;
    private final CategoryGroupService categoryGroupService;
    private final NotificationService notificationService;
    private final ProductDetailInfoService productDetailInfoService;
    private final ProductImageServie productImageServie;
    private final CategoryDetailInfoService categoryDetailInfoService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final FeedbackService feedbackService;

    private Shop getShop() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        List<Shop> shops = user.getShops();
        if (shops.size() == 0) {
            return null;
        }

        return shops.get(0);
    }

    @PostMapping("/create")
    public ResponseEntity<Shop> createShop(@RequestBody ShopCreateRequest request) {
        Shop shop = getShop();
        if (shop != null) {
            return ResponseEntity.badRequest().build();
        }

        User user = (User) authenticatedManager.getAuthenticatedUser();

        Province pr = Province.builder()
                .id(request.getProvinceId())
                .name(request.getProvinceName())
                .build();
        Province province = provinceService.save(pr);

        District dt = District.builder()
                .id(request.getDistrictId())
                .name(request.getDistrictName())
                .build();
        District district = districtService.save(dt);

        Ward wd = Ward.builder()
                .id(request.getWardId())
                .name(request.getWardName())
                .build();
        Ward ward = wardService.save(wd);

        ShopAddress shopAddress = ShopAddress.builder()
                .province(province)
                .district(district)
                .ward(ward)
                .specificAddress(request.getSpecific_address())
                .build();
        shopAddressService.save(shopAddress);

        Shop shopRequest = Shop.builder()
                .user(user)
                .name(request.getName())
                .shopImage("/api/v1/publics/shop/image/-1")
                .shopAddress(shopAddress)
                .joinTime(new Date())
                .build();

        return ResponseEntity.ok().body(shopService.save(shopRequest));
    }

    @GetMapping("/products")
    public List<Product> getProducts(
            @RequestParam("page") Optional<Integer> pageId,
            @RequestParam("filter") Optional<String> flt
    ) {
        Shop shop = getShop();
        Integer page = pageId.orElse(1) - 1;
        String filter = flt.orElse("default");
        List<Product> products = productService.getByOwnShop(shop, page, filter);
        return products;
    }

    @PostMapping("/products/upload")
    public ResponseEntity<String> uploadProduct(@RequestParam("product") String jsonRequest, @RequestParam("images") MultipartFile[] images, @RequestParam("video") MultipartFile video) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        var request = gsonUtils.fromJson(jsonRequest, ProductRequest.class);
        CategoryGroup group = categoryGroupService.getCategoryGroupById(request.getCategoryGroup());

        var productRequest = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .uploadTime(new Date())
                .price(request.getPrice())
                .available(request.getAvailable())
                .shop(shop)
                .categoryGroup(group)
                .build();

        var product = productService.save(productRequest);

        if (video != null) {
            productVideoStorageService.store(video, product.getId() + ".mp4");
            product.setVideo("/api/v1/publics/product/video/" + product.getId());
            productService.save(product);
        }

        int count = 0;
        var services = (ProductImageStorageService) productImageStorageService;
        for (MultipartFile image : images) {
            count++;
            services.store(image, product.getId().toString(), count + ".jpg");
            var pI = ProductImage.builder()
                    .product(product)
                    .url("/api/v1/publics/product/image/" + product.getId() + "?imgId=" + count)
                    .build();
            productImageServie.save(pI);
        }

        List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(product.getCategoryGroup().getCategory());
        categoryDetailInfos.forEach(it -> {
            var productDetail = request.getProductDetailRequests()
                    .stream()
                    .filter(cc -> cc.getName().equalsIgnoreCase(it.getName()))
                    .findFirst().orElse(null);

            var pdi = ProductDetailInfo.builder()
                    .categoryDetailInfo(it)
                    .value(productDetail.getValue())
                    .product(product)
                    .build();
            productDetailInfoService.save(pdi);
        });

        shop.getSubscriptions().forEach(it -> {
            User userr = it.getUser();
            Notification notification = Notification.builder()
                    .title(String.format("Shop %s vừa upload sản phẩm mới", shop.getName()))
                    .content(product.getDescription().substring(0, 100))
                    .imageUrl(product.getImages().get(0).getUrl())
                    .user(userr)
                    .createdAt(new Date())
                    .read(false)
                    .build();
            notificationService.save(notification);
        });

        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/search")
    public ResponseEntity<List<Order>> searchOrder(
            @RequestParam("keyword") Optional<String> kw,
            @RequestParam("filter") Optional<OrderStatus> ft,
            @RequestParam("page") Optional<Integer> pg
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        String keyword = kw.orElse("");
        OrderStatus filter = ft.orElse(null);
        Integer page = pg.orElse(1) - 1;
        List<Order> orders = null;
        if (filter == null) {
            orders = orderService.getByShopAndId(shop, keyword, page);
            return ResponseEntity.ok().body(orders);
        }

        orders = orderService.getByShopAndStatusAndId(shop, filter, keyword, page);
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryDTO>> getCategoryInShop() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        var dtos = categoryService.getCategoryInShop(shop)
                .stream()
                .map(it -> it.toDto())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/category/revenue")
    public ResponseEntity<List<Integer>> getRevenueOfCategoryInShop() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        var revenues = categoryService.getRevenueOfCategoryAnalystInDay(shop);

        return ResponseEntity.ok().body(revenues);
    }

    @GetMapping("/revenue/real-time")
    public ResponseEntity<List<Integer>> getRevenueInDay() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Integer> revenues = new ArrayList<>();

        var curDay = orderService.getRevenueAnalystInDay(shop);
        var prevDay = orderService.getRevenueAnalystInPreviousDay(shop);

        int revenueCurDay = 0;
        int revenuePrevDay = 0;

        for (Integer i : curDay) {
            if (i == null) i = 0;
            revenueCurDay += i;
        }
        revenues.add(revenueCurDay);

        for (Integer i : prevDay) {
            if (i == null) i = 0;
            revenuePrevDay += i;
        }
        revenues.add(revenuePrevDay);

        return ResponseEntity.ok().body(revenues);
    }

    @PostMapping("/order/confirm/{id}")
    public ResponseEntity<String> confirmOrder(@PathVariable("id") String orderId) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Order order = orderService.getById(orderId);
        order.setStatus(OrderStatus.SHIPPING);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/reject/{id}")
    public ResponseEntity<String> rejectOrder(@PathVariable("id") String orderId) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Order order = orderService.getById(orderId);
        order.setStatus(OrderStatus.CANCELLED);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/special/process/{id}")
    public ResponseEntity<String> confirmSpecialOrder(
            @PathVariable("id") String orderId,
            @RequestParam("shippingFee") Optional<Integer> sF,
            @RequestParam("action") String action
    ) {
        Shop shop = getShop();
        Order order = orderService.getById(orderId);
        if (shop == null || order == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer shippingFee = sF.orElse(0);
        order.setShippingFee(shippingFee);

        if (action.equals("CONFIRM"))
        {
            order.setStatus(OrderStatus.SPECIAL_USER);
        }

        if (action.equals("REJECT"))
        {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotification() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(shop.getNotifications());
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getFeedback(
            @RequestParam("rate") Integer rate,
            @RequestParam("page") Optional<Integer> pg
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer page = pg.orElse(1) - 1;
        List<Feedback> feedbacks = null;
        if (rate == null) {
            feedbacks = feedbackService.getByShop(shop, page);
        } else feedbacks = feedbackService.getByShopAndRate(shop, rate, page);

        List<FeedbackDTO> feedbackDtos = feedbacks.stream()
                .map(it -> it.toDto())
                .toList();
        return ResponseEntity.ok().body(feedbackDtos);
    }
}
