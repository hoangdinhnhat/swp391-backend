/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.publics;

import com.google.gson.Gson;
import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.category.CategoryDTO;
import com.swp391.backend.model.category.CategoryDetails;
import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfo;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.categoryGroup.CategoryGroupDTO;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.categoryGroup.CategoryGroupSold;
import com.swp391.backend.model.message.Message;
import com.swp391.backend.model.message.MessageService;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.notification.NotificationService;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductDTO;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.product.SearchDTO;
import com.swp391.backend.model.productAttachWith.AttachWithService;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfoService;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImageService;
import com.swp391.backend.model.productFeedbackReply.FeedbackReplyService;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productImage.ProductImageServie;
import com.swp391.backend.model.productSale.ProductSaleDTO;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.saleEvent.SaleEvent;
import com.swp391.backend.model.saleEvent.SaleEventService;
import com.swp391.backend.model.settings.Setting;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopDetails;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.subscription.SubscriptionService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.utils.storage.ProductImageStorageService;
import com.swp391.backend.utils.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/publics")
public class PublicController {

    private final ProductService productService;
    private final AttachWithService attachWithService;
    private final CategoryGroupService categoryGroupService;
    private final ShopService shopService;
    private final SaleEventService saleEventService;
    private final ProductSaleService productSaleService;
    private final FeedbackService feedbackService;
    private final CategoryDetailInfoService categoryDetailInfoService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductImageServie productImageServie;
    private final ProductDetailInfoService productDetailInfoService;
    private final ProductFeedbackImageService productFeedbackImageService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;
    private final FeedbackReplyService feedbackReplyService;
    private final MessageService messageService;
    private final SettingService settingService;

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

    @Autowired
    @Qualifier(value = "productFeedbackVideo")
    private StorageService productFeedbackVideoStorageService;

    @Autowired
    @Qualifier(value = "productFeedbackImage")
    private StorageService productFeedbackImageStorageService;

    @Autowired
    @Qualifier(value = "messageImage")
    private StorageService messageImageStorageService;

    @Autowired
    @Qualifier(value = "messageVideo")
    private StorageService messageVideoStorageService;

    @GetMapping("/user/avatar/{email}")
    public ResponseEntity<Resource> getProductImage(@PathVariable("email") String email) {
        User user = (User) userService.loadUserByUsername(email);
        String avatarFile = user.getId() + ".avatar";
        Resource file = storageService.loadAsResource(avatarFile);
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
        header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
        return ResponseEntity.ok().headers(header).body(file);
    }

    @GetMapping("/time")
    public Date getTime() {
        return new Date();
    }

    @GetMapping("/category/all")
    public List<CategoryDTO> getAllCategory() {
        var categories = categoryService.getAll().stream()
                .map(it -> it.toDto())
                .collect(Collectors.toList());
        return categories;
    }

    @GetMapping("/category/group/{id}")
    public List<CategoryGroupDTO> getAllCategoryGroup(@PathVariable("id") Integer categoryId) {
        List<CategoryGroup> categoryGroups = categoryService.getById(categoryId).getCategoryGroups();
        return categoryGroups.stream()
                .map(t -> t.toDTO())
                .collect(Collectors.toList());
    }

    @GetMapping("/category/{category_id}")
    public CategoryDetails getCategory(@PathVariable("category_id") Integer category_id, @RequestParam("page") Optional<Integer> pageOpt, @RequestParam("filter") Optional<String> flt) {
        Category category = categoryService.getById(category_id);
        Integer page = pageOpt.orElse(1) - 1;
        String filter = flt.orElse("default");
        List<ProductSaleDTO> products = productService.getByCategory(category, page, filter, 40).stream()
                .map(it -> {
                    var find = productSaleService.findProductInSale(it);
                    if (find != null) {
                        return find.toDto();
                    } else {
                        return ProductSaleDTO.builder()
                                .product(it)
                                .build();
                    }
                }).collect(Collectors.toList());

        List<ShopDTO> shops = shopService.topThreeShopInCategory(category)
                .stream()
                .map(it -> it.toDto())
                .collect(Collectors.toList());

        int maxPage = productService.getMaxPage(category);

        return CategoryDetails
                .builder()
                .shops(shops)
                .ps(products)
                .categoryName(category.getName())
                .maxPage(maxPage)
                .build();
    }

    @GetMapping("/category/category-group/{category_group_id}")
    public List<ProductDTO> getCategoryGroup(@PathVariable("category_group_id") Integer category_group_id) {
        var categoryGroup = categoryGroupService.getCategoryGroupById(category_group_id);
        List<Product> products = categoryGroup.getProducts();

        return products.stream().map(it -> {
            ProductSaleDTO productSaleDto = null;

            var productSale = productSaleService.findProductInSale(it);
            if (productSale != null) {
                productSaleDto = productSale.toDto();
                productSaleDto.setProduct(null);
            }

            return ProductDTO.builder()
                    .id(it.getId())
                    .name(it.getName())
                    .images(it.getImages())
                    .categoryGroup(categoryGroup.toDTO())
                    .price(it.getPrice())
                    .rating(it.getRating())
                    .sold(it.getSold())
                    .productSale(productSaleDto)
                    .build();
        }).collect(Collectors.toList());
    }

    @GetMapping("/category/details/{id}")
    public ResponseEntity<List<CategoryDetailInfo>> getCategoryDetail(@PathVariable("id") Integer categoryId) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(category);
        return ResponseEntity.ok().body(categoryDetailInfos);
    }

    @GetMapping("/product/all")
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("/product/daily")
    public List<ProductDTO> getDailyProduct() {
        List<Product> products = productService.getAllProduct();
        List<Integer> dailies = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            float floatRan = Math.round(Math.random() * (products.size() - 1) + 1);
            int ranNum = Math.round(floatRan);
            while (dailies.indexOf(ranNum) != -1) {
                floatRan = Math.round(Math.random() * (products.size() - 1) + 1);
                ranNum = Math.round(floatRan);
            }
            dailies.add(ranNum);
        }
        List<Product> daily = productService.getAllProduct()
                .stream()
                .filter(it -> (dailies.indexOf(it.getId()) != -1))
                .collect(Collectors.toList());

        return daily.stream().map(it -> {
            ProductSaleDTO productSaleDto = null;

            var productSale = productSaleService.findProductInSale(it);
            if (productSale != null) {
                productSaleDto = productSale.toDto();
                productSaleDto.setProduct(null);
            }

            return ProductDTO.builder()
                    .id(it.getId())
                    .name(it.getName())
                    .images(it.getImages())
                    .price(it.getPrice())
                    .rating(it.getRating())
                    .sold(it.getSold())
                    .productSale(productSaleDto)
                    .build();
        }).collect(Collectors.toList());
    }

    @GetMapping("/product/top-ten")
    public List<Product> getTopTenProduct() {
        return productService.getSoldTopTenProduct();
    }

    @GetMapping("/product/search")
    public ResponseEntity<SearchDTO> search(@RequestParam("keyword") String keyword, @RequestParam("page") Optional<Integer> pageOpt, @RequestParam("filter") Optional<String> flt) {
        Integer page = pageOpt.orElse(1) - 1;
        String filter = flt.orElse("default");
        Shop shop = shopService.searchShop(keyword);
        ProductSaleDTO productSaleDto = null;
        List<ProductSaleDTO> products = productService.searchProduct(keyword, page, filter).stream()
                .map(it -> {
                    var find = productSaleService.findProductInSale(it);
                    if (find != null) {
                        return find.toDto();
                    } else {
                        return ProductSaleDTO.builder()
                                .product(it)
                                .build();
                    }
                }).collect(Collectors.toList());
        ShopDTO dto = null;
        if (shop != null) dto = shop.toDto();

        int maxPage = productService.getMaxPage(keyword);

        var searchDTO = SearchDTO.builder()
                .shop(dto)
                .products(products)
                .maxPage(maxPage)
                .build();
        return ResponseEntity.ok().body(searchDTO);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> productDetail(
            @PathVariable("id") Optional<Integer> id,
            @RequestParam("page") Optional<Integer> pg
    ) {
        Integer pid = id.orElse(1);
        Integer page = pg.orElse(1) - 1;

        var p = productService.getProductById(pid);
        var cg = categoryGroupService.getCategoryGroupById(p.getCategoryGroup().getId());
        var shop = shopService.getShopById(p.getShop().getId());
        var numOfAttach = settingService.getById(3).getValue();
        var numOfRelated = settingService.getById(2).getValue();

        List<CategoryGroup> attachWith = attachWithService.getAttachWith(cg);
        List<Product> products = categoryGroupService.getProductByCategoryGroups(p, attachWith, numOfAttach);
        List<ProductSaleDTO> relatedTo = productService.getByCategory(cg.getCategory(), 0, "default", numOfRelated)
                .stream()
                .map(it -> {
                    var find = productSaleService.findProductInSale(it);
                    if (find != null) {
                        return find.toDto();
                    } else {
                        return ProductSaleDTO.builder()
                                .product(it)
                                .build();
                    }
                }).collect(Collectors.toList());

        var feedbacks = feedbackService.getByProduct(p, page)
                .stream()
                .map(i -> i.toDto())
                .collect(Collectors.toList());

        ProductSaleDTO productSaleDto = null;
        var productSale = productSaleService.findProductInSale(p);
        if (productSale != null) {
            productSaleDto = productSale.toDto();
            productSaleDto.setProduct(null);
        }

        var productDTO = ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .sold(p.getSold())
                .price(p.getPrice())
                .available(p.getAvailable())
                .rating(p.getRating())
                .category(cg.getCategory().toDto())
                .categoryGroup(cg.toDTO())
                .video(p.getVideo())
                .images(p.getImages())
                .productSale(productSaleDto)
                .feedbacks(feedbacks)
                .productDetailInfos(p.getProductDetailInfos())
                .shop(shop.toDto())
                .attachWiths(products)
                .relatedTo(relatedTo)
                .build();

        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping("/product/upload")
    public ResponseEntity<String> uploadProduct(@RequestParam("product") String jsonRequest, @RequestParam("images") MultipartFile[] images, @RequestParam("video") MultipartFile video) {
        var request = gsonUtils.fromJson(jsonRequest, ProductRequest.class);
        CategoryGroup group = categoryGroupService.getCategoryGroupById(request.getCategoryGroup());
        var productRequest = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .video("/api/v1/publics/product/video/" + 80)
                .uploadTime(new Date())
                .price(request.getPrice())
                .available(request.getAvailable())
                .shop(shopService.getShopById(1))
                .categoryGroup(group)
                .build();

        var product = productService.save(productRequest);

        productVideoStorageService.store(video, product.getId() + ".mp4");

        int count = 0;
        var services = (ProductImageStorageService) productImageStorageService;
        for (MultipartFile image : images) {
            count++;
            services.store(image, "" + product.getId(), count + ".jpg");
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

        Shop shop = shopService.getShopById(1);

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

    @GetMapping("/notifications/{user_id}")
    public List<Notification> getNotifications(@PathVariable("user_id") String email) {
        User user = (User) userService.loadUserByUsername(email);
        return user.getNotifications();
    }

    @GetMapping("/notifications/read")
    public ResponseEntity<String> setReadNotification() {
        User user = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");
        List<Notification> notifications = notificationService.getNotificationByUser(user).stream()
                .map(it -> {
                    it.setRead(true);
                    return it;
                })
                .toList();
        notificationService.saveAllAndFlush(notifications);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/day/{shop_id}")
    public List<Integer> dataByHour(@PathVariable("shop_id") Integer shopId) {
        Shop shop = shopService.getShopById(shopId);
        return orderService.getNumberOfOrderAnalystInDay(shop);
    }

    @GetMapping("/orders/week/{shop_id}")
    public List<Integer> dataByWeek(@PathVariable("shop_id") Integer shopId) {
        Shop shop = shopService.getShopById(shopId);
        return orderService.getNumberOfOrderAnalystInWeek(shop);
    }

    @GetMapping("/orders/month/{shop_id}")
    public List<Integer> dataByMonth(@PathVariable("shop_id") Integer shopId) {
        Shop shop = shopService.getShopById(shopId);
        return orderService.getNumberOfOrderAnalystInMonth(shop);
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<Resource> getProductImage(@PathVariable("id") Integer id, @RequestParam("imgId") Optional<Integer> imgId) {
        Product product = productService.getProductById(id);
        if (product != null) {
            String image = product.getId() + "/" + imgId.orElse(1) + ".jpg";
            Resource file = productImageStorageService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/video/{id}")
    public ResponseEntity<Resource> getProductVideo(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            String image = product.getId() + ".mp4";
            Resource file = productVideoStorageService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/feedbacks/video/{feedback_id}")
    public ResponseEntity<Resource> getProductFeedbackVideo(@PathVariable("feedback_id") Integer feedbackId) {
        Feedback feedback = feedbackService.getById(feedbackId);
        if (feedback != null) {
            String video = feedback.getId() + ".mp4";
            Resource file = productFeedbackVideoStorageService.loadAsResource(video);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/feedbacks/image/{feedback_id}")
    public ResponseEntity<Resource> getProductFeedbackImage(@PathVariable("feedback_id") Integer feedbackId, @RequestParam("imgId") Integer imgId) {
        Feedback feedback = feedbackService.getById(feedbackId);
        if (feedback != null) {
            String video = feedback.getId() + "/" + imgId + ".jpg";
            Resource file = productFeedbackImageStorageService.loadAsResource(video);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/shop/{id}")
    public ShopDetails shopDetail(
            @PathVariable("id") Optional<Integer> id,
            @RequestParam("page") Optional<Integer> pageId,
            @RequestParam("filter") Optional<String> flt
    ) {
        Integer shopId = id.orElse(1);
        Integer page = pageId.orElse(1) - 1;
        String filter = flt.orElse("default");
        Shop shop = shopService.getShopById(shopId);
        List<Product> products = productService.getByShop(shop, page, filter);
        int maxPage = productService.getMaxPage(shop);

        return ShopDetails
                .builder()
                .shopDetails(shop.toDto())
                .maxPage(maxPage)
                .products(products)
                .build();
    }

    @GetMapping("/shop/image/{id}")
    public ResponseEntity<Resource> getShopImage(@PathVariable("id") Integer id) {
        Shop shop = shopService.getShopById(id);
        if (shop != null) {
            String image = "shop/" + shop.getId() + ".jpg";
            Resource file = storageService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return ResponseEntity.ok().headers(header).body(file);
        } else {
            String image = "shop/default.jpg";
            Resource file = storageService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return ResponseEntity.ok().headers(header).body(file);
        }
    }

    @GetMapping("/shop/trending")
    public List<ShopDTO> shopTrending() {
        return shopService.getShopTrending().stream().map(it -> it.toDto()).collect(Collectors.toList());
    }

    @GetMapping("/event/{sale_event}")
    public List<ProductSaleDTO> saleEventDetail(
            @PathVariable("sale_event") Integer sale_event,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("priority") Optional<Integer> priority,
            @RequestParam("filter") Optional<String> flt
    ) {
        SaleEvent event = saleEventService.getSaleEventById(sale_event);
        Integer pageId = page.orElse(1) - 1;
        Integer priorityId = priority.orElse(-1);
        String filter = flt.orElse("all");
        var product = productService.getProductById(priorityId);
        List<ProductSaleDTO> finded = productSaleService.getBySaleEvent(event, pageId)
                .stream()
                .map(i -> i.toDto())
                .collect(Collectors.toList());

        ProductSaleDTO priorityFind = null;
        if (product != null) {
            priorityFind = finded.stream().filter(it -> it.getProduct().getId() == product.getId()).findFirst().orElse(null);
        }

        List<ProductSaleDTO> result = new ArrayList<>();

        if (priorityFind != null) {
            result.add(priorityFind);
            result.addAll(
                    finded.stream().
                            filter(it -> it.getProduct().getId() != product.getId())
                            .collect(Collectors.toList())
            );
        } else result = finded;

        if (filter.equals("all")) {
            return result;
        }

        return result
                .stream()
                .filter(it -> {
                    String category = it.getProduct().getCategoryGroup().getCategory().getName();
                    return category.toLowerCase().equals(filter.toLowerCase());
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/test/cate/{id}")
    public List<CategoryGroupSold> getTest(@PathVariable("id") Integer shopId) {
        return categoryGroupService.getTopThreeSoldCategoryGroupInDay(shopService.getShopById(2));
    }

    @GetMapping("/message/image/{id}")
    public ResponseEntity<Resource> getMessageImage(@PathVariable("id") Integer id, @RequestParam("imgId") Optional<Integer> imgId) {
        Message message = messageService.getById(id);
        if (message != null) {
            String image = message.getId() + "/" + imgId.orElse(1) + ".jpg";
            Resource file = messageImageStorageService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/message/video/{id}")
    public ResponseEntity<Resource> getMessageVideo(@PathVariable("id") Integer id) {
        Message message = messageService.getById(id);
        if (message != null) {
            String video = message.getId() + ".mp4";
            Resource file = messageVideoStorageService.loadAsResource(video);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }
}
