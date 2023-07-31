package com.swp391.backend.controllers.shop;

import com.google.gson.Gson;
import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.controllers.publics.ProductRequest;
import com.swp391.backend.model.category.CategoryDTO;
import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfo;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.counter.Counter;
import com.swp391.backend.model.counter.CounterService;
import com.swp391.backend.model.district.District;
import com.swp391.backend.model.district.DistrictService;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.notification.NotificationService;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderDTO;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.order.OrderStatus;
import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.orderDetails.OrderDetailsId;
import com.swp391.backend.model.orderDetails.OrderDetailsService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfoService;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackDTO;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productFeedbackReply.FeedbackReply;
import com.swp391.backend.model.productFeedbackReply.FeedbackReplyService;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productImage.ProductImageServie;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.province.ProvinceService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.shopAddress.ShopAddress;
import com.swp391.backend.model.shopAddress.ShopAddressService;
import com.swp391.backend.model.shopPackage.ShopPackage;
import com.swp391.backend.model.shopPackage.ShopPackageService;
import com.swp391.backend.model.shopPlan.ShopPlan;
import com.swp391.backend.model.shopPlan.ShopPlanService;
import com.swp391.backend.model.subscription.SubscriptionService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.model.ward.Ward;
import com.swp391.backend.model.ward.WardService;
import com.swp391.backend.utils.ghn.GHNService;
import com.swp391.backend.utils.storage.ProductImageStorageService;
import com.swp391.backend.utils.storage.StorageService;
import com.swp391.backend.utils.zalopay_gateway.ZaloPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {

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
    private final SubscriptionService subscriptionService;
    private final FeedbackReplyService feedbackReplyService;
    private final OrderDetailsService orderDetailsService;
    private final ShopPackageService shopPackageService;
    private final ShopPlanService shopPlanService;
    private final CounterService counterService;
    private final UserService userService;
    private final GHNService ghnService;
    private final ZaloPayService zaloPayService = ZaloPayService.gI();

    @Autowired
    private Gson gsonUtils;
    @Autowired
    @Qualifier(value = "avatar")
    private StorageService storageService;
    @Autowired
    @Qualifier(value = "productImage")
    private StorageService productImageStorageService;
    @Autowired
    @Qualifier(value = "productVideo")
    private StorageService productVideoStorageService;

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
                .phone(request.getPhone())
                .build();

        Shop savedShop = shopService.save(shopRequest);

        ShopPlan shopPlan = shopPlanService.getByPlan("UNREGISTE");
        ShopPackage shopPackage = ShopPackage.builder()
                .shopPlan(shopPlan)
                .shop(savedShop)
                .build();
        shopPackageService.save(shopPackage);

        return ResponseEntity.ok().body(savedShop);
    }

    @GetMapping("/package/")
    public ResponseEntity<Boolean> getShopPlan() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isTrial = shop.getShopPackages()
                .stream()
                .anyMatch(pk -> pk.getShopPlan().getPlan().equals("TRIAL"));

        return ResponseEntity.ok().body(isTrial);
    }

    @GetMapping("/package/check")
    public ResponseEntity<Boolean> checkShopPackage() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isValid = shopPackageService.checkShopPackage(shop);
        return ResponseEntity.ok().body(isValid);
    }

    @PostMapping("/registe/payment")
    public ResponseEntity<String> registePayment(@RequestParam("plan") String plan) throws Exception {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isValid = shopPackageService.checkShopPackage(shop);
        if (isValid) return ResponseEntity.badRequest().build();

        ShopPlan shopPlan = shopPlanService.getByPlan(plan);
        String gatewayUrl = zaloPayService.createGatewayUrl(Math.round(shopPlan.getPrice() * 23000), "shop/registe?plan=" + plan);
        return ResponseEntity.ok().body(gatewayUrl);
    }

    @PostMapping("/registe/trial")
    public ResponseEntity<String> registeShopTrial(HttpServletRequest request, @RequestParam("plan") String plan) throws Exception {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isTrial = shop.getShopPackages()
                .stream()
                .anyMatch(pk -> pk.getShopPlan().getPlan().equals("FREE TRIAL"));
        if (isTrial) return ResponseEntity.badRequest().build();

        ShopPlan shopPlan = shopPlanService.getByPlan(plan);

        ShopPackage shopPackage = ShopPackage.builder()
                .shopPlan(shopPlan)
                .shop(shop)
                .build();
        shopPackageService.save(shopPackage);
        shopService.save(shop);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/registe")
    public ModelAndView registeShop(HttpServletRequest request, @RequestParam("plan") String plan) throws Exception {
        Shop shop = getShop();
        boolean check = zaloPayService.checkCallback(request);
        if (shop == null || !check) {
            return null;
        }

        ShopPlan shopPlan = shopPlanService.getByPlan(plan);

        var counter = counterService.getById("WALLET");
        counter.setValue(counter.getValue() + shopPlan.getPrice());
        counterService.save(counter);

        ShopPackage shopPackage = ShopPackage.builder()
                .shopPlan(shopPlan)
                .shop(shop)
                .build();
        shopPackageService.save(shopPackage);
        shopService.save(shop);

        return new ModelAndView("redirect:http://localhost:3000/seller/portal/product/all");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateShopInfo(
            @RequestParam("image") Optional<MultipartFile> img,
            @RequestParam("name") Optional<String> name,
            @RequestParam("phone") Optional<String> phone
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        var image = img.orElse(null);
        var shopName = name.orElse(null);
        var phoneNumber = phone.orElse(null);

        if (image != null) {
            storageService.store(image, "shop/" + shop.getId() + ".jpg");
            shop.setShopImage("/api/v1/publics/shop/image/" + shop.getId());
        }

        if (shopName != null && !shopName.trim().isEmpty()) {
            shop.setName(shopName);
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            shop.setPhone(phoneNumber);
        }

        shopService.save(shop);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/max-page")
    public ResponseEntity<Integer> getProductMaxPage() {
        Shop shop = getShop();
        int maxPage = productService.getMaxPageOwnShop(shop);
        return ResponseEntity.ok().body(maxPage);
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

    @GetMapping("/products/ban")
    public List<Product> getBanProducts(
            @RequestParam("page") Optional<Integer> pageId,
            @RequestParam("filter") Optional<String> flt
    ) {
        Shop shop = getShop();
        Integer page = pageId.orElse(1) - 1;
        String filter = flt.orElse("default");
        List<Product> products = productService.getByOwnShopAndBan(shop, page, filter);
        return products;
    }

    @GetMapping("/products/update/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable("id") Integer pId,
            @RequestParam("product") String jsonRequest,
            @RequestParam("images") Optional<MultipartFile[]> imgs,
            @RequestParam("video") Optional<MultipartFile> vd
    ) {
        Shop shop = getShop();
        Product product = productService.getProductById(pId);
        if (shop == null || product == null) {
            return ResponseEntity.badRequest().build();
        }

        var request = gsonUtils.fromJson(jsonRequest, ProductRequest.class);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setAvailable(request.getAvailable());

        productService.save(product);

        MultipartFile video = vd.orElse(null);
        if (video != null) {
            productVideoStorageService.store(video, product.getId() + ".mp4");
            product.setVideo("/api/v1/publics/product/video/" + product.getId());
            productService.save(product);
        }

        var images = imgs.orElse(null);
        if (images != null) {
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
        }

        var categoryDetailInfos = categoryDetailInfoService.getByCategory(product.getCategoryGroup().getCategory());
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
                    .title(String.format("Shop %s vừa thay đổi thông tin sản phẩm", shop.getName()))
                    .content(product.getDescription().substring(0, 100))
                    .imageUrl("/api/v1/publics/product/image/" + product.getId() + "?imgId=1")
                    .redirectUrl("/product?productId=" + product.getId())
                    .user(userr)
                    .createdAt(new Date())
                    .read(false)
                    .build();
            notificationService.save(notification);
        });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/upload")
    public ResponseEntity<String> uploadProduct(
            @RequestParam("product") String jsonRequest,
            @RequestParam("images") MultipartFile[] images,
            @RequestParam("video") Optional<MultipartFile> vd
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isValid = shopPackageService.checkShopPackage(shop);
        if (!isValid) return ResponseEntity.badRequest().build();

        var request = gsonUtils.fromJson(jsonRequest, ProductRequest.class);
        if (request.getAvailable() <= 0 || request.getPrice() <= 0) {
            return ResponseEntity.badRequest().build();
        }
//        int size = shop.getShopPackages().size();
//        ShopPackage shopPackage = shop.getShopPackages().get(size - 1);
//        ShopPlan shopPlan = shopPackage.getShopPlan();
//        if (request.getPrice() > shopPlan.getPrice())
//        {
//            return ResponseEntity.badRequest().build();
//        }
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

        var video = vd.orElse(null);
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
                    .value(productDetail.getValue().trim().isEmpty() ? "No Provided" : productDetail.getValue())
                    .product(product)
                    .build();
            productDetailInfoService.save(pdi);
        });

        shop.getSubscriptions().forEach(it -> {
            User userr = it.getUser();
            Notification notification = Notification.builder()
                    .title(String.format("Shop %s just upload a new product!", shop.getName()))
                    .content(product.getDescription().substring(0, 100))
                    .imageUrl("/api/v1/publics/product/image/" + product.getId() + "?imgId=1")
                    .redirectUrl("/product?productId=" + product.getId())
                    .user(userr)
                    .createdAt(new Date())
                    .read(false)
                    .build();
            notificationService.save(notification);
        });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/edit")
    public ResponseEntity<String> editProduct(
            @RequestParam("product") String jsonRequest,
            @RequestParam("productId") Integer productId,
            @RequestParam("images") Optional<MultipartFile[]> imgs,
            @RequestParam("video") Optional<MultipartFile> vd
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        boolean isValid = shopPackageService.checkShopPackage(shop);
        if (!isValid) return ResponseEntity.badRequest().build();

        var request = gsonUtils.fromJson(jsonRequest, ProductRequest.class);
        if (request.getAvailable() <= 0 || request.getPrice() <= 0) {
            return ResponseEntity.badRequest().build();
        }
//        int size = shop.getShopPackages().size();
//        ShopPackage shopPackage = shop.getShopPackages().get(size - 1);
//        ShopPlan shopPlan = shopPackage.getShopPlan();
//        if (request.getPrice() > shopPlan.getPrice())
//        {
//            return ResponseEntity.badRequest().build();
//        }

        Product findedProduct = productService.getProductById(productId);

        findedProduct.setName(request.getName());
        findedProduct.setDescription(request.getDescription());
        findedProduct.setPrice(request.getPrice());
        findedProduct.setAvailable(request.getAvailable());
        var product = productService.save(findedProduct);

        var video = vd.orElse(null);
        if (video != null) {
            productVideoStorageService.store(video, product.getId() + ".mp4");
            product.setVideo("/api/v1/publics/product/video/" + product.getId());
            productService.save(product);
        }

        var images = imgs.orElse(null);
        if (images != null) {
            int count = 0;
            var services = (ProductImageStorageService) productImageStorageService;
            var productImages = productImageServie.getByProduct(product);
            productImageServie.deleteAll(productImages);

            for (MultipartFile image : images) {
                count++;
                services.store(image, product.getId().toString(), count + ".jpg");
                var pI = ProductImage.builder()
                        .product(product)
                        .url("/api/v1/publics/product/image/" + product.getId() + "?imgId=" + count)
                        .build();
                productImageServie.save(pI);
            }
        }

        shop.getSubscriptions().forEach(it -> {
            User userr = it.getUser();
            Notification notification = Notification.builder()
                    .title(String.format("Shop %s just update the product's information", shop.getName()))
                    .content(String.format("%s TO %s", findedProduct.getName(), product.getName()))
                    .imageUrl("/api/v1/publics/product/image/" + product.getId() + "?imgId=1")
                    .redirectUrl("/product?productId=" + product.getId())
                    .user(userr)
                    .createdAt(new Date())
                    .read(false)
                    .build();
            notificationService.save(notification);
        });

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam("id") Optional<Integer> pId) {
        Integer productId = pId.orElse(-1);
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        product.setShop(null);
        product.setBan(true);
        product.setBan(true);
        productService.save(product);
        return ResponseEntity.ok().body("Delete product successfully!");
    }

    @GetMapping("/orders/max-page")
    public ResponseEntity<Integer> getOrderMaxPage(
            @RequestParam("keyword") Optional<String> kw,
            @RequestParam("filter") Optional<OrderStatus> ft
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        String keyword = kw.orElse("");
        OrderStatus filter = ft.orElse(null);
        Integer maxPage = orderService.getMaxPage(shop, keyword);

        if (filter != null) {
            maxPage = orderService.getMaxPage(shop, filter, keyword);
        }

        return ResponseEntity.ok().body(maxPage);
    }

    @GetMapping("/orders/number")
    public ResponseEntity<Integer> getNumOfOrder(
            @RequestParam("keyword") Optional<String> kw,
            @RequestParam("filter") Optional<OrderStatus> ft
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        String keyword = kw.orElse("");
        OrderStatus filter = ft.orElse(null);
        Integer num = orderService.getNumOfOrder(shop, keyword);

        if (filter != null) {
            num = orderService.getNumOfOrder(shop, filter, keyword);
        }

        return ResponseEntity.ok().body(num);
    }

    @GetMapping("/orders/search")
    public ResponseEntity<List<OrderDTO>> searchOrder(
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
        List<OrderDTO> orders = null;
        if (filter == null) {
            orders = orderService.getByShopAndId(shop, keyword, page)
                    .stream()
                    .map(it -> it.toDto())
                    .toList();
            return ResponseEntity.ok().body(orders);
        }

        orders = orderService.getByShopAndStatusAndId(shop, filter, keyword, page)
                .stream()
                .map(it -> it.toDto())
                .toList();
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

    @GetMapping("/analyst/real-time")
    public ResponseEntity<List<RealTimeData>> getRevenueInDay() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RealTimeData> result = new ArrayList<>();

        var curDay = orderService.getRevenueAnalystInDay(shop);
        var prevDay = orderService.getRevenueAnalystInPreviousDay(shop);
        RealTimeData realTimeData = RealTimeData
                .builder()
                .prev(prevDay)
                .cur(curDay)
                .build();
        result.add(realTimeData);

        var curDay2 = orderService.getNumberOfOrderAnalystInDay(shop);
        var prevDay2 = orderService.getNumberOfOrderAnalystInPreviousDay(shop);
        RealTimeData realTimeData2 = RealTimeData
                .builder()
                .prev(prevDay2)
                .cur(curDay2)
                .build();
        result.add(realTimeData2);

        var curDay3 = subscriptionService.getNumberOfSubscriptionAnalystInDay(shop);
        var prevDay3 = subscriptionService.getNumberOfSubscriptionAnalystInPreviousDay(shop);
        RealTimeData realTimeData3 = RealTimeData
                .builder()
                .prev(prevDay3)
                .cur(curDay3)
                .build();
        result.add(realTimeData3);

        var curDay4 = feedbackService.getNumberOfFeedbackAnalystInDay(shop);
        var prevDay4 = feedbackService.getNumberOfFeedbackAnalystInPreviousDay(shop);
        RealTimeData realTimeData4 = RealTimeData
                .builder()
                .prev(prevDay4)
                .cur(curDay4)
                .build();
        result.add(realTimeData4);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/analyst/day")
    public ResponseEntity<List<RealTimeData>> getAnalystInDay() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RealTimeData> result = new ArrayList<>();

        var curDay = orderService.getRevenueAnalystInDay(shop);
        var prevDay = orderService.getRevenueAnalystInPreviousDay(shop);
        RealTimeData realTimeData = RealTimeData
                .builder()
                .prev(prevDay)
                .cur(curDay)
                .build();
        result.add(realTimeData);

        var curDay2 = orderDetailsService.getNumberOfSoldProductAnalystInDay(shop);
        var prevDay2 = orderDetailsService.getNumberOfSoldProductAnalystInPreviousDay(shop);
        RealTimeData realTimeData2 = RealTimeData
                .builder()
                .prev(prevDay2)
                .cur(curDay2)
                .build();
        result.add(realTimeData2);

        var curDay3 = orderService.getNumberOfOrderAnalystInDay(shop);
        var prevDay3 = orderService.getNumberOfOrderAnalystInPreviousDay(shop);
        RealTimeData realTimeData3 = RealTimeData
                .builder()
                .prev(prevDay3)
                .cur(curDay3)
                .build();
        result.add(realTimeData3);

        var curDay4 = subscriptionService.getNumberOfSubscriptionAnalystInDay(shop);
        var prevDay4 = subscriptionService.getNumberOfSubscriptionAnalystInPreviousDay(shop);
        RealTimeData realTimeData4 = RealTimeData
                .builder()
                .prev(prevDay4)
                .cur(curDay4)
                .build();
        result.add(realTimeData4);

        var curDay5 = feedbackService.getNumberOfFeedbackAnalystInDay(shop);
        var prevDay5 = feedbackService.getNumberOfFeedbackAnalystInPreviousDay(shop);
        RealTimeData realTimeData5 = RealTimeData
                .builder()
                .prev(prevDay5)
                .cur(curDay5)
                .build();
        result.add(realTimeData5);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/analyst/week")
    public ResponseEntity<List<RealTimeData>> getAnalystInWeek() {

        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RealTimeData> result = new ArrayList<>();

        var curWeek = orderService.getRevenueAnalystInWeek(shop);
        var prevWeek = orderService.getRevenueAnalystInPreviousWeek(shop);
        RealTimeData realTimeData = RealTimeData
                .builder()
                .prev(prevWeek)
                .cur(curWeek)
                .build();
        result.add(realTimeData);

        var curDay2 = orderDetailsService.getNumberOfSoldProductAnalystInWeek(shop);
        var prevDay2 = orderDetailsService.getNumberOfSoldProductAnalystInPreviousWeek(shop);
        RealTimeData realTimeData2 = RealTimeData
                .builder()
                .prev(prevDay2)
                .cur(curDay2)
                .build();
        result.add(realTimeData2);

        var curWeek3 = orderService.getNumberOfOrderAnalystInWeek(shop);
        var prevWeek3 = orderService.getNumberOfOrderAnalystInPreviousWeek(shop);
        RealTimeData realTimeData3 = RealTimeData
                .builder()
                .prev(prevWeek3)
                .cur(curWeek3)
                .build();
        result.add(realTimeData3);

        var curWeek4 = subscriptionService.getNumberOfSubscriptionAnalystInWeek(shop);
        var prevWeek4 = subscriptionService.getNumberOfSubscriptionAnalystInPreviousWeek(shop);
        RealTimeData realTimeData4 = RealTimeData
                .builder()
                .prev(prevWeek4)
                .cur(curWeek4)
                .build();
        result.add(realTimeData4);

        var curDay5 = feedbackService.getNumberOfFeedbackAnalystInWeek(shop);
        var prevDay5 = feedbackService.getNumberOfFeedbackAnalystInPreviousWeek(shop);
        RealTimeData realTimeData5 = RealTimeData
                .builder()
                .prev(prevDay5)
                .cur(curDay5)
                .build();
        result.add(realTimeData5);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/analyst/month")
    public ResponseEntity<List<RealTimeData>> getAnalystInMonth() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RealTimeData> result = new ArrayList<>();

        var curWeek = orderService.getRevenueAnalystInMonth(shop);
        var prevWeek = orderService.getRevenueAnalystInPreviousMonth(shop);
        RealTimeData realTimeData = RealTimeData
                .builder()
                .prev(prevWeek)
                .cur(curWeek)
                .build();
        result.add(realTimeData);

        var curDay2 = orderDetailsService.getNumberOfSoldProductAnalystInMonth(shop);
        var prevDay2 = orderDetailsService.getNumberOfSoldProductAnalystInPreviousMonth(shop);
        RealTimeData realTimeData2 = RealTimeData
                .builder()
                .prev(prevDay2)
                .cur(curDay2)
                .build();
        result.add(realTimeData2);

        var curWeek3 = orderService.getNumberOfOrderAnalystInMonth(shop);
        var prevWeek3 = orderService.getNumberOfOrderAnalystInPreviousMonth(shop);
        RealTimeData realTimeData3 = RealTimeData
                .builder()
                .prev(prevWeek3)
                .cur(curWeek3)
                .build();
        result.add(realTimeData3);

        var curWeek4 = subscriptionService.getNumberOfSubscriptionAnalystInMonth(shop);
        var prevWeek4 = subscriptionService.getNumberOfSubscriptionAnalystInPreviousMonth(shop);
        RealTimeData realTimeData4 = RealTimeData
                .builder()
                .prev(prevWeek4)
                .cur(curWeek4)
                .build();
        result.add(realTimeData4);

        var curDay5 = feedbackService.getNumberOfFeedbackAnalystInMonth(shop);
        var prevDay5 = feedbackService.getNumberOfFeedbackAnalystInPreviousMonth(shop);
        RealTimeData realTimeData5 = RealTimeData
                .builder()
                .prev(prevDay5)
                .cur(curDay5)
                .build();
        result.add(realTimeData5);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/analyst/year")
    public ResponseEntity<List<RealTimeData>> getAnalystInYear() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RealTimeData> result = new ArrayList<>();

        var curWeek = orderService.getRevenueAnalystInYear(shop);
        var prevWeek = orderService.getRevenueAnalystInPreviousYear(shop);
        RealTimeData realTimeData = RealTimeData
                .builder()
                .prev(prevWeek)
                .cur(curWeek)
                .build();
        result.add(realTimeData);

        var curDay2 = orderDetailsService.getNumberOfSoldProductAnalystInYear(shop);
        var prevDay2 = orderDetailsService.getNumberOfSoldProductAnalystInPreviousYear(shop);
        RealTimeData realTimeData2 = RealTimeData
                .builder()
                .prev(prevDay2)
                .cur(curDay2)
                .build();
        result.add(realTimeData2);

        var curWeek3 = orderService.getNumberOfOrderAnalystInYear(shop);
        var prevWeek3 = orderService.getRevenueAnalystInPreviousYear(shop);
        RealTimeData realTimeData3 = RealTimeData
                .builder()
                .prev(prevWeek3)
                .cur(curWeek3)
                .build();
        result.add(realTimeData3);

        var curWeek4 = subscriptionService.getNumberOfSubscriptionAnalystInYear(shop);
        var prevWeek4 = subscriptionService.getNumberOfSubscriptionAnalystInPreviousYear(shop);
        RealTimeData realTimeData4 = RealTimeData
                .builder()
                .prev(prevWeek4)
                .cur(curWeek4)
                .build();
        result.add(realTimeData4);

        var curDay5 = feedbackService.getNumberOfFeedbackAnalystInYear(shop);
        var prevDay5 = feedbackService.getNumberOfFeedbackAnalystInPreviousYear(shop);
        RealTimeData realTimeData5 = RealTimeData
                .builder()
                .prev(prevDay5)
                .cur(curDay5)
                .build();
        result.add(realTimeData5);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/analyst/constant")
    public ResponseEntity<Map<String, List<Integer>>> getConstantAnalyst() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, List<Integer>> rs = new HashMap<>();
        List<Integer> categoryQuantity = categoryService.getNumberOfCategoryAnalyst(shop);
        List<Integer> categoryRevenue = categoryService.getRevenueOfCategoryAnalyst(shop);
        List<Integer> saleQuantity = orderDetailsService.getNumberOfSoldProductAnalystInYear(shop);

        rs.put("cq", categoryQuantity);
        rs.put("cr", categoryRevenue);
        rs.put("sq", saleQuantity);
        return ResponseEntity.ok().body(rs);
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
        Product product = order.getOrderDetails().get(0).getProduct();
        User user = order.getUser();

        Notification notification = Notification.builder()
                .imageUrl(user.getImageurl())
                .user(user)
                .createdAt(new Date())
                .read(false)
                .build();

        if (action.equals("CONFIRM")) {
            notification.setTitle("Your application has been approved!");
            notification.setContent(String.format("Shop %s has confirmed your bird purchase and has set a shipping fee. Please reconfirm your order!", shop.getName()));
            notification.setRedirectUrl("/purchase/contact");
            order.setShippingFee(shippingFee);
            order.setStatus(OrderStatus.SPECIAL_USER);
        }

        if (action.equals("REJECT")) {
            notification.setTitle("Your application has been rejected by the shop!");
            notification.setContent(String.format("Shop %s rejected your bird application for various reasons!", shop.getName()));
            notification.setRedirectUrl("/purchase/cancel");
            product.setAvailable(product.getAvailable() + 1);
            order.setStatus(OrderStatus.CANCELLED);
        }

        productService.save(product);
        notificationService.save(notification);
        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/special/delivery/{id}")
    public ResponseEntity<String> processDeliverySpecialOrder(
            @PathVariable("id") String orderId,
            @RequestParam("action") String action
    ) {

        Order order = orderService.getById(orderId);
        Shop shop = getShop();
        if (order == null || shop == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = order.getUser();

        Notification notificationUser = Notification.builder()
                .imageUrl(shop.getShopImage())
                .user(user)
                .createdAt(new Date())
                .read(false)
                .build();


        if (action.equals("CONFIRM")) {
            notificationUser.setTitle("Delivery successful!");
            notificationUser.setContent("Your order has been successfully delivered to you. Thanks for your support!");
            notificationUser.setRedirectUrl("/purchase/complete");
            order.setStatus(OrderStatus.COMPLETED);

        }

        if (action.equals("REJECT")) {
            notificationUser.setTitle("Delivery failed!");
            notificationUser.setContent("You have refused to accept the application. If there is any problem with the goods, we apologize!");
            notificationUser.setRedirectUrl("/purchase/cancel");
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderService.save(order);

        notificationService.save(notificationUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/max-page")
    public ResponseEntity<Integer> getMaxPage() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        Integer maxPage = notificationService.getMaxPage(shop);
        return ResponseEntity.ok().body(maxPage);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotification(@RequestParam("page") Optional<Integer> pg) {
        Integer page = pg.orElse(1) - 1;
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Notification> notifications = notificationService.getNotificationByShop(shop, page);
        return ResponseEntity.ok().body(notifications);
    }

    @PostMapping("/notification/read/{id}")
    public ResponseEntity<String> markRead(@PathVariable("id") Integer notiId) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        Notification notification = notificationService.getById(notiId);
        notification.setRead(true);
        notificationService.save(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notification/read")
    public ResponseEntity<String> markReadAll() {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Notification> notifications = notificationService.getNotificationByShop(shop).stream()
                .map(it -> {
                    it.setRead(true);
                    return it;
                })
                .toList();
        notificationService.saveAllAndFlush(notifications);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feedbacks/max-feedback")
    public ResponseEntity<Integer> getMaxFeedback(
            @RequestParam("rate") Optional<Integer> rt
    ) {
        Integer rate = rt.orElse(null);
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }
        Integer maxPage = feedbackService.getMaxFeedback(shop);
        if (rate != null) {
            maxPage = feedbackService.getMaxFeedback(shop, rate);
        }

        return ResponseEntity.ok().body(maxPage);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<List<FeedbackDTO>> getFeedback(
            @RequestParam("rate") Optional<Integer> rt,
            @RequestParam("page") Optional<Integer> pg
    ) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Integer page = pg.orElse(1) - 1;
        Integer rate = rt.orElse(null);
        List<Feedback> feedbacks = null;
        if (rate == null) {
            feedbacks = feedbackService.getByShop(shop, page);
        } else feedbacks = feedbackService.getByShopAndRate(shop, rate, page);

        List<FeedbackDTO> feedbackDtos = feedbacks.stream()
                .map(it -> it.toDto())
                .toList();
        return ResponseEntity.ok().body(feedbackDtos);
    }

    @PostMapping("/feedbacks/accept/{id}")
    public ResponseEntity<String> shopAcceptRefund(@PathVariable("id") Integer fId) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Feedback feedback = feedbackService.getById(fId);
        feedback.setProcessed(true);
        String expectedOrderId = feedback.getOrderId() + "_RF";
        Order order = orderService.getById(feedback.getOrderId());
        Order refundOrder = orderService.getById(expectedOrderId);

        OrderDetailsId findId = new OrderDetailsId();
        findId.setOrderId(order.getId());
        findId.setProductId(feedback.getProduct().getId());
        OrderDetails findOd =  orderDetailsService.getById(findId);
        double priceRefund = 0;

        if (refundOrder == null)
        {
            priceRefund += order.getShippingFee();
            refundOrder = Order.builder()
                    .id(expectedOrderId)
                    .user(order.getUser())
                    .receiveInfo(order.getReceiveInfo())
                    .status(OrderStatus.REFUND)
                    .payment(order.getPayment())
                    .description("REFUND ORDER OF #" + order.getId())
                    .shippingFee(order.getShippingFee())
                    .shop(shop)
                    .createdTime(new Date())
                    .build();
            orderService.save(refundOrder);
        }

        OrderDetailsId id = new OrderDetailsId();
        id.setOrderId(refundOrder.getId());
        id.setProductId(feedback.getProduct().getId());

        OrderDetails orderDetails = OrderDetails.builder()
                .product(feedback.getProduct())
                .quantity(findOd.getQuantity())
                .sellPrice(findOd.getSellPrice())
                .soldPrice(findOd.getSoldPrice())
                .order(refundOrder)
                .id(id)
                .build();

        if (order.getPayment().equals("ZaloPay Wallet"))
        {
            Counter counter = counterService.getById("WALLET");
            User user = feedback.getUser();
            user.setWallet(user.getWallet() + findOd.getSoldPrice() * findOd.getQuantity() + priceRefund);
            counter.setValue(counter.getValue() - findOd.getSoldPrice() * findOd.getQuantity() - priceRefund);

            userService.save(user);
            counterService.save(counter);
        }

        Notification notification = Notification.builder()
                .title(String.format("Shop %s agrees to your refund request.", shop.getName()))
                .content("Due to careless delivery, the shop agrees to your return request.")
                .imageUrl(shop.getShopImage())
                .redirectUrl("/purchase/refund")
                .user(feedback.getUser())
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);

        orderDetailsService.saveRefund(orderDetails);
        orderDetailsService.delete(findOd);

        List<OrderDetails> ods = orderDetailsService.getByOrder(refundOrder);
        String date = ghnService.shippingOrders(shop, refundOrder.getReceiveInfo(), refundOrder, ods, "KHONGCHOXEMHANG", "REFUND ORDER: " + refundOrder.getId());
        refundOrder.setExpectedReceive(date);
        orderService.save(refundOrder);

        feedbackService.save(feedback);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/feedbacks/reject/{id}")
    public ResponseEntity<String> shopRejectRefund(@PathVariable("id") Integer fId) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Feedback feedback = feedbackService.getById(fId);
        feedback.setProcessed(true);

        Notification notification = Notification.builder()
                .title(String.format("Shop %s declined your request for a refund.", shop.getName()))
                .content("Due to the reason that the items were delivered with no errors from the shop, the shop refused to request a refund.")
                .imageUrl(shop.getShopImage())
                .redirectUrl("/seller/portal/order/complete")
                .user(feedback.getUser())
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);

        feedbackService.save(feedback);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/feedbacks/response/")
    public ResponseEntity<String> shopResponseFeedback(@RequestBody ShopResponseRequest request) {
        Shop shop = getShop();
        if (shop == null) {
            return ResponseEntity.badRequest().build();
        }

        Feedback feedback = feedbackService.getById(request.getFeedbackId());
        feedback.setProcessed(true);

        var feedbackRep = FeedbackReply.builder()
                .feedback(feedback)
                .content(request.getResponse())
                .build();

        feedbackReplyService.save(feedbackRep);

        Notification notification = Notification.builder()
                .title(String.format("Shop %s just reply your feedback", shop.getName()))
                .content(request.getResponse())
                .imageUrl("/api/v1/publics/product/image/" + feedback.getProduct().getId() + "?imgId=1")
                .redirectUrl("/product?productId=" + feedback.getProduct().getId())
                .user(feedback.getUser())
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);
        feedbackService.save(feedback);
        return ResponseEntity.ok().build();
    }
}
