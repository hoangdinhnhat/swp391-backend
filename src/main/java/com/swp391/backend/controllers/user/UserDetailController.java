/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import com.google.gson.Gson;
import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.controllers.publics.FeedbackRequest;
import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.cart.CartItem;
import com.swp391.backend.model.cart.CartService;
import com.swp391.backend.model.cartProduct.CartProduct;
import com.swp391.backend.model.cartProduct.CartProductDTO;
import com.swp391.backend.model.cartProduct.CartProductKey;
import com.swp391.backend.model.cartProduct.CartProductService;
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
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImageService;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.province.ProvinceService;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.receiveinfo.ReceiveInfoService;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.report.ReportRequest;
import com.swp391.backend.model.report.ReportService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.subscription.Subscription;
import com.swp391.backend.model.subscription.SubscriptionId;
import com.swp391.backend.model.subscription.SubscriptionService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.model.ward.Ward;
import com.swp391.backend.model.ward.WardService;
import com.swp391.backend.utils.ghn.GHNService;
import com.swp391.backend.utils.mail.EmailSender;
import com.swp391.backend.utils.mail.OrderConfirmationTemplate;
import com.swp391.backend.utils.storage.ProductFeedbackImageStorageService;
import com.swp391.backend.utils.storage.StorageService;
import com.swp391.backend.utils.zalopay_gateway.ZaloPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lenovo
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserDetailController {

    private final UserService userService;
    private final AuthenticatedManager authenticatedManager;
    private final PasswordEncoder passwordEncoder;
    private final ReceiveInfoService receiveInfoService;
    private final CartProductService cartProductService;
    private final CartService cartService;
    private final ProductService productService;
    private final ShopService shopService;
    private final ProductSaleService productSaleService;
    private final ProvinceService provinceService;
    private final DistrictService districtService;
    private final WardService wardService;
    private final SubscriptionService subscriptionService;
    private final OrderService orderService;
    private final OrderDetailsService orderDetailsService;
    private final NotificationService notificationService;
    private final FeedbackService feedbackService;
    private final ProductFeedbackImageService productFeedbackImageService;
    private final TemporaryStorage temporaryStorage;
    private final ReportService reportService;
    private final EmailSender gmailSender;
    private final GHNService ghnService;
    private final CounterService counterService;
    private ZaloPayService zaloPayService = ZaloPayService.gI();

    @Autowired
    private Gson gsonUtils;

    @Autowired
    @Qualifier(value = "avatar")
    private StorageService storageService;

    @Autowired
    @Qualifier(value = "productFeedbackVideo")
    private StorageService productFeedbackVideoStorageService;

    @Autowired
    @Qualifier(value = "productFeedbackImage")
    private StorageService productFeedbackImageStorageService;

    @GetMapping("/info")
    public ResponseEntity<UserDTO> loginUserInfo() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        var defaultInfo = receiveInfoService.getDefaultReceiveInfo(user);
        int page = receiveInfoService.getMaxPage(user);
        List<CartProduct> cartProducts = null;
        Cart cart = user.getCart();
        if (cart != null) {
            cartProducts = cartProductService.getCartProductByCart(cart, 1);
        }

        List<Shop> shops = user.getShops();
        ShopDTO shop = null;
        if (shops.size() > 0) {
            shop = shops.get(0).toDto();
        }

        List<Integer> shopSubscription = subscriptionService.getByUser(user)
                .stream()
                .map(it -> it.getShop().getId())
                .toList();

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .imageurl(user.getImageurl())
                .gender(user.getGender())
                .shopDTO(shop)
                .wallet(user.getWallet())
                .receiveInfoPage(page)
                .defaultReceiveInfo(defaultInfo)
                .cartProducts(cartProducts)
                .shopSubscription(shopSubscription)
                .build();
        return ResponseEntity.ok().body(userDTO);
    }

    @Transactional
    @PostMapping("/info/update/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateProfileRequest request) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setGender(request.getGender());
        userService.save(user);
        return ResponseEntity.ok("Update profile successfully!");
    }

    @Transactional
    @PostMapping("/info/update/password")
    public ResponseEntity<String> updatePassword(@RequestBody ChangePasswordRequest request) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        String old_password = request.getOldpassword();
        String new_password = request.getNewpassword();
        boolean isMatch = passwordEncoder.matches(old_password, user.getPassword());
        if (!isMatch) {
            throw new IllegalStateException("Old Password is incorrect!");
        }
        user.setPassword(passwordEncoder.encode(new_password));
        userService.save(user);
        return ResponseEntity.ok("Change password successfully!");
    }

    @PostMapping("/info/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        user.setImageurl("/api/v1/users/info/avatar");
        userService.save(user);
        storageService.store(file, user.getId() + ".avatar");
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/info/avatar")
    public ResponseEntity<Resource> getAvatar() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        String avatarFile = user.getId() + ".avatar";
        Resource file = storageService.loadAsResource(avatarFile);
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
        header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
        return ResponseEntity.ok().headers(header).body(file);
    }

    @PostMapping("/info/receives")
    public ResponseEntity<ReceiveInfo> addReceiveInfo(@RequestBody ReceiveInfoRequest request) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        boolean defaultInfo = request.is_default();
        if (receiveInfoService.getReceiveInfo(user, 0).size() == 0) {
            defaultInfo = true;
        }

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

        var receiveInfo = ReceiveInfo.builder()
                .fullname(request.getFullname())
                .phone(request.getPhone())
                .province(province)
                .district(district)
                .ward(ward)
                .specific_address(request.getSpecific_address())
                ._default(defaultInfo)
                .user(user)
                .build();
        return ResponseEntity.ok().body(receiveInfoService.saveReceiveInfo(receiveInfo));
    }

    @GetMapping("/info/receives/all")
    public ResponseEntity<List<ReceiveInfo>> getReceiveInfo() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        return ResponseEntity.ok().body(receiveInfoService.getReceiveInfo(user));
    }

    @GetMapping("/info/receives")
    public ResponseEntity<List<ReceiveInfo>> getReceiveInfo(@RequestParam("page") Integer pageNum) {
        pageNum--;
        User user = (User) authenticatedManager.getAuthenticatedUser();
        return ResponseEntity.ok().body(receiveInfoService.getReceiveInfo(user, pageNum));
    }

    @PostMapping("/info/receives/default/{receive_id}")
    public ResponseEntity<ReceiveInfo> setReceiveInfoDefault(@PathVariable("receive_id") Integer id) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        ReceiveInfo info = receiveInfoService.getReceiveInfo(id);
        info.set_default(true);
        List<ReceiveInfo> infos = receiveInfoService.getReceiveInfo(user).stream()
                .filter(r -> r.getId() != id)
                .map(r -> {
                    r.set_default(false);
                    return r;
                }).collect(Collectors.toList());
        receiveInfoService.saveReceiveInfo(info);
        receiveInfoService.saveReceiveInfos(infos);
        return ResponseEntity.ok().body(info);
    }

    @DeleteMapping("/info/receives/delete/{delete_id}")
    public ResponseEntity<String> deleteReceiveInfo(@PathVariable("delete_id") Integer id) {
        receiveInfoService.deleteReceiveInfo(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subscription/{shop_id}")
    public ResponseEntity<Subscription> subscribeShop(@PathVariable("shop_id") Integer shopId) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Shop shop = shopService.getShopById(shopId);

        if (shop == null) return ResponseEntity.badRequest().build();

        SubscriptionId id = new SubscriptionId();
        id.setShopId(shop.getId());
        id.setUserId(user.getId());

        Subscription subscription = Subscription.builder()
                .id(id)
                .shop(shop)
                .user(user)
                .subscriptionTime(new Date())
                .build();
        subscription = subscriptionService.save(subscription);

        Notification notification = Notification.builder()
                .title("Your shop just gained a new following")
                .content(String.format("Customer %s just followed your shop. Congratulations on your new follower!", user.getFirstname()))
                .imageUrl(user.getImageurl())
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);

        return ResponseEntity.ok().body(subscription);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItem>> getCart(@RequestParam("page") Optional<Integer> page) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Integer pageId = page.orElse(1) - 1;
        Cart cart = user.getCart();
        if (cart == null) {
            ResponseEntity.badRequest().build();
        }
        HashMap<Integer, List<CartProductDTO>> mapper = new HashMap<>();
        cartProductService.getCartProductByCart(cart, pageId)
                .forEach(i -> {
                    Integer shopId = i.getProduct().getShop().getId();
                    Shop shop = shopService.getShopById(shopId);
                    if (mapper.get(shop.getId()) == null) {
                        List<CartProductDTO> products = new ArrayList<>();


                        CartProductDTO dto = null;
                        ProductSale pSale = productSaleService.findProductInSale(i.getProduct());

                        if (pSale != null) {
                            dto = CartProductDTO.builder()
                                    .saleQuantity(pSale.getSaleQuantity())
                                    .saleSold(pSale.getSold())
                                    .product(i.getProduct())
                                    .salePercent(pSale.getSaleEvent().getPercent())
                                    .quantity(i.getQuantity())
                                    .build();
                        } else {
                            dto = CartProductDTO.builder()
                                    .saleQuantity(0)
                                    .saleSold(0)
                                    .product(i.getProduct())
                                    .salePercent(0)
                                    .quantity(i.getQuantity())
                                    .build();
                        }

                        products.add(dto);
                        mapper.put(shop.getId(), products);
                    } else {
                        List<CartProductDTO> products = mapper.get(shop.getId());
                        ProductSale pSale = productSaleService.findProductInSale(i.getProduct());

                        CartProductDTO dto = null;

                        if (pSale != null) {
                            dto = CartProductDTO.builder()
                                    .saleQuantity(pSale.getSaleQuantity())
                                    .saleSold(pSale.getSold())
                                    .product(i.getProduct())
                                    .salePercent(pSale.getSaleEvent().getPercent())
                                    .quantity(i.getQuantity())
                                    .build();
                        } else {
                            dto = CartProductDTO.builder()
                                    .saleQuantity(0)
                                    .saleSold(0)
                                    .product(i.getProduct())
                                    .salePercent(0)
                                    .quantity(i.getQuantity())
                                    .build();
                        }

                        products.add(dto);
                        mapper.put(shop.getId(), products);
                    }
                });
        List<CartItem> cartItems = new ArrayList<>();
        mapper.keySet().forEach(it -> {
            List<CartProductDTO> value = mapper.get(it);
            var cartItem = CartItem.builder()
                    .shop(shopService.getShopById(it).toDto())
                    .cartProducts(value)
                    .build();
            cartItems.add(cartItem);
        });
        return ResponseEntity.ok().body(cartItems);
    }

    @PostMapping("/cart/{product_id}")
    public ResponseEntity<String> addProductToCart(@PathVariable("product_id") Integer product_id, @RequestParam("quantity") Optional<Integer> quan) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Cart cart = user.getCart();
        Integer quantity = quan.orElse(1);
        if (cart == null) {
            cart = new Cart();
            cartService.save(cart);
            user.setCart(cart);
            userService.save(user);
        }
        Product product = productService.getProductById(product_id);
        if (product.getShop().getUser().getId() == user.getId() || product.isBan()) {
            return ResponseEntity.badRequest().build();
        }
        CartProduct findedCartProduct = cartProductService.findByCartAndProduct(cart, product);

        if (findedCartProduct != null) {
            quantity += findedCartProduct.getQuantity();
        }

        if (quantity > product.getAvailable())
        {
            return ResponseEntity.badRequest().body("This product is out of stock");
        }

        CartProductKey id = new CartProductKey();
        id.cartId = cart.getId();
        id.productId = product.getId();

        var cartProduct = CartProduct.builder()
                .product(product)
                .id(id)
                .cart(cart)
                .quantity(quantity)
                .build();

        cartProductService.save(cartProduct);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/{product_id}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable("product_id") Integer product_id) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Cart cart = user.getCart();
        if (cart == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productService.getProductById(product_id);

        CartProductKey id = new CartProductKey();
        id.cartId = cart.getId();
        id.productId = product.getId();

        cartProductService.delete(id);
        return ResponseEntity.ok().build();
    }

    public String generateOrderId() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonthValue());
        String date = String.valueOf(LocalDateTime.now().getDayOfMonth());

        return year + month + date;
    }

    @GetMapping("/payment/finish")
    public ModelAndView processAfterPayment(HttpServletRequest request) throws Exception {
        boolean check = zaloPayService.checkCallback(request);
        if (!check) {
            return null;
        }

        List<CheckOutRequest> checkOutRequests = (List<CheckOutRequest>) temporaryStorage.getTemporaryObject();
        List<Order> orders = new ArrayList<>();
        orderCreator(checkOutRequests, OrderStatus.SHIPPING, orders);
        User user = (User) authenticatedManager.getAuthenticatedUser();
        orders = orders.stream().map(o -> {
            List<OrderDetails> orderDetails = orderDetailsService.getByOrder(o);
            o.setOrderDetails(orderDetails);
            return o;
        }).toList();
        String template = OrderConfirmationTemplate.getTemplete(user.getFirstname(), orders);
        gmailSender.send("Order Confirmation", template, user.getEmail());
        return new ModelAndView("redirect:http://localhost:3000/purchase/shipping");
    }

    public int convertUsdToVnd(float usd) {
        return Math.round(usd * 23000);
    }

    @PostMapping("/payment/open")
    public ResponseEntity<String> openGateway(@RequestParam("total") float total, @RequestBody List<CheckOutRequest> checkOutRequests) throws Exception {
        temporaryStorage.saveTemporaryObject(checkOutRequests);
        String gatewayUrl = zaloPayService.createGatewayUrl(convertUsdToVnd(total), "users/payment/finish");
        return ResponseEntity.ok().body(gatewayUrl);
    }

    public double roundedFloat(double num)
    {
        return (double) Math.round(num * 100) / 100;
    }

    @Transactional
    public void orderCreator(List<CheckOutRequest> checkOutRequests, OrderStatus orderStatus, List<Order> orders) throws Exception {
        int numberOfOrder = orderService.getNumberOfOrderInCurrentDay();
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Cart cart = user.getCart();
        checkOutRequests.forEach(it -> {
            int index = checkOutRequests.indexOf(it) + 1;
            String oId = generateOrderId() + (numberOfOrder + index);
            Shop shop = shopService.getShopById(it.getShopId());
            Product product = productService.getProductById(it.getCheckOutItems().get(0).getProductId());
            String description = product.getDescription().substring(0, 150);
            ReceiveInfo receiveInfo = receiveInfoService.getReceiveInfo(it.getReceiveInfo());

            Order order = Order.builder()
                    .id(oId)
                    .user(user)
                    .receiveInfo(receiveInfo)
                    .status(orderStatus)
                    .payment(it.getPayment())
                    .description(description)
                    .shippingFee(roundedFloat(it.getShippingFee()))
                    .shop(shop)
                    .createdTime(new Date())
                    .build();

            orderService.save(order);

            it.getCheckOutItems().forEach(tt -> {

                OrderDetailsId id = new OrderDetailsId();
                id.setOrderId(order.getId());
                id.setProductId(tt.getProductId());
                Product product1 = productService.getProductById(tt.getProductId());

                OrderDetails orderDetails = OrderDetails.builder()
                        .product(product1)
                        .quantity(tt.getQuantity())
                        .order(order)
                        .id(id)
                        .build();
                orderDetailsService.save(orderDetails);
                try {
                    product1.setAvailable(product1.getAvailable() - tt.getQuantity());
                    productService.save(product1);
                } catch (IllegalStateException e) {
                    throw new IllegalStateException("Product " + product1.getName() + " available isn't enough");
                }

                CartProductKey cpi = new CartProductKey();
                cpi.cartId = cart.getId();
                cpi.productId = product1.getId();

                cartProductService.delete(cpi);
            });
            orders.add(order);
            List<OrderDetails> ods = orderDetailsService.getByOrder(order);
            String date = ghnService.shippingOrders(shop, receiveInfo, order, ods, "CHOXEMHANGKHONGTHU", "ORDER: " + order.getId());
            order.setExpectedReceive(date);
            orderService.save(order);
            if ("ZaloPay Wallet".equals(it.getPayment())) {
                Counter wallet = counterService.getById("WALLET");
                wallet.setValue(wallet.getValue() + order.getSoldPrice() + order.getShippingFee());
            }

            Notification notification = Notification.builder()
                    .title("Your shop just received a new order!")
                    .content(String.format("Customer %s has just placed an order in your shop. Please check and feedback!", user.getFirstname()))
                    .imageUrl(product.getImages().get(0).getUrl())
                    .redirectUrl("/seller/portal/order/all")
                    .shop(product.getShop())
                    .createdAt(new Date())
                    .read(false)
                    .build();
            notificationService.save(notification);
        });
    }

    @PostMapping("/order/create")
    public ResponseEntity<String> createOrder(@RequestBody List<CheckOutRequest> checkOutRequests) throws Exception {
        List<Order> orders = new ArrayList<>();
        orderCreator(checkOutRequests, OrderStatus.PENDING, orders);
        User user = (User) authenticatedManager.getAuthenticatedUser();
        orders = orders.stream().map(o -> {
            List<OrderDetails> orderDetails = orderDetailsService.getByOrder(o);
            o.setOrderDetails(orderDetails);
            return o;
        }).toList();
        String template = OrderConfirmationTemplate.getTemplete(user.getFirstname(), orders);
        gmailSender.send("Order Confirmation", template, user.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/special/create")
    public ResponseEntity<String> createSpecialOrder(@RequestParam("id") Integer productId, @RequestParam("quantity") Optional<Integer> quan) throws Exception {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Product product = productService.getProductById(productId);
        ReceiveInfo receiveInfo = receiveInfoService.getDefaultReceiveInfo(user);
        if (product.getCategoryGroup().getCategory().getId() != 1 || receiveInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        Shop shop = shopService.getShopById(product.getShop().getId());
        Integer quantity = quan.orElse(1);
        int numberOfOrder = orderService.getNumberOfOrderInCurrentDay();
        String oId = generateOrderId() + (numberOfOrder + 1);

        Order order = Order.builder()
                .id(oId)
                .user(user)
                .status(OrderStatus.SPECIAL_SHOP)
                .receiveInfo(receiveInfo)
                .payment("COD")
                .description(product.getDescription().substring(0, 150))
                .shop(shop)
                .special(true)
                .createdTime(new Date())
                .build();

        orderService.save(order);

        OrderDetailsId id = new OrderDetailsId();
        id.setOrderId(order.getId());
        id.setProductId(product.getId());

        OrderDetails orderDetails = OrderDetails.builder()
                .product(product)
                .quantity(quantity)
                .order(order)
                .id(id)
                .build();
        orderDetailsService.save(orderDetails);

        try {
            product.setAvailable(product.getAvailable() - 1);
            productService.save(product);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Product " + product.getName() + " available isn't enough");
        }


        Notification notification = Notification.builder()
                .title("Your shop has just received a special order")
                .content(String.format("Customer %s just sent a request to buy birds. Please respond quickly.", user.getFirstname()))
                .imageUrl(product.getImages().get(0).getUrl())
                .redirectUrl("/seller/portal/order/contact")
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);

        List<Order> orders = new ArrayList<>();
        List<OrderDetails> orderDetailss = orderDetailsService.getByOrder(order);
        order.setOrderDetails(orderDetailss);
        orders.add(order);
        String template = OrderConfirmationTemplate.getTemplete(user.getFirstname(), orders);
        gmailSender.send("Order Confirmation", template, user.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/special/process/{id}")
    public ResponseEntity<String> processSpecialOrder(
            @PathVariable("id") String orderId,
            @RequestParam("action") String action
    ) {

        Order order = orderService.getById(orderId);
        User user = (User) authenticatedManager.getAuthenticatedUser();
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = order.getOrderDetails().get(0).getProduct();
        Shop shop = order.getShop();

        Notification notification = Notification.builder()
                .imageUrl(user.getImageurl())
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();

        if (action.equals("CONFIRM")) {
            notification.setTitle("The customer has confirmed the bird purchase order!");
            notification.setContent(String.format("Customer %s has once again confirmed the bird order. Hurry up to deliver to customers!", user.getFirstname()));
            notification.setRedirectUrl("/seller/portal/order/shipping");
            order.setStatus(OrderStatus.SHIPPING);
        }

        if (action.equals("REJECT")) {
            notification.setTitle("The customer has just refused a special order that your shop has set the shipping fee.");
            notification.setContent(String.format("Customer %s has just rejected an order to buy birds because it doesn't accept the shipping fee.", user.getFirstname()));
            notification.setRedirectUrl("/seller/portal/order/cancel");
            product.setAvailable(product.getAvailable() + 1);
            order.setStatus(OrderStatus.CANCELLED);
        }

        productService.save(product);

        orderService.save(order);

        notificationService.save(notification);


        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/max-page")
    public ResponseEntity<Integer> getMaxPage() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Integer maxPage = notificationService.getMaxPage(user);
        return ResponseEntity.ok().body(maxPage);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotification(@RequestParam("page") Optional<Integer> pg) {
        Integer page = pg.orElse(1) - 1;
        User user = (User) authenticatedManager.getAuthenticatedUser();
        List<Notification> notifications = notificationService.getNotificationByUser(user, page);
        return ResponseEntity.ok().body(notifications);
    }

    @PostMapping("/notification/read/{id}")
    public ResponseEntity<String> markRead(@PathVariable("id") Integer notiId) {
        Notification notification = notificationService.getById(notiId);
        notification.setRead(true);
        notificationService.save(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notification/read")
    public ResponseEntity<String> markReadAll() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        List<Notification> notifications = notificationService.getNotificationByUser(user).stream()
                .map(it -> {
                    it.setRead(true);
                    return it;
                })
                .toList();
        notificationService.saveAllAndFlush(notifications);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/orders/search")
    public ResponseEntity<List<OrderDTO>> searchOrder(
            @RequestParam("keyword") Optional<String> kw,
            @RequestParam("filter") Optional<OrderStatus> ft,
            @RequestParam("page") Optional<Integer> pg
    ) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        String keyword = kw.orElse("");
        OrderStatus filter = ft.orElse(null);
        Integer page = pg.orElse(1) - 1;
        List<OrderDTO> orders = null;
        if (filter == null) {
            orders = orderService.getByUserAndId(user, keyword, page)
                    .stream()
                    .map(it -> it.toDto())
                    .toList();

            return ResponseEntity.ok().body(orders);
        }

        orders = orderService.getByUserAndStatusAndId(user, filter, keyword, page)
                .stream()
                .map(it -> it.toDto())
                .toList();

        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/order/feedbacks")
    public ResponseEntity<String> uploadProductFeedback(
            @RequestParam("feedback") String jsonRequest,
            @RequestParam("images") Optional<MultipartFile[]> imgs,
            @RequestParam("video") Optional<MultipartFile> vd
    ) {
        var images = imgs.orElse(null);
        var video = vd.orElse(null);

        var request = gsonUtils.fromJson(jsonRequest, FeedbackRequest.class);
        var order = orderService.getById(request.getOrderId());
        var product = productService.getProductById(request.getProductId());
        var user = (User) authenticatedManager.getAuthenticatedUser();
        var orderDetails = orderDetailsService.getByOrderAndProduct(order, product);

        var feedbackRequest = Feedback.builder()
                .rate(request.getRate())
                .time(request.getTime())
                .type("RATE PRODUCT")
                .description(request.getDescription())
                .product(product)
                .user(user)
                .build();
        Feedback feedback = feedbackService.save(feedbackRequest);

        if (video != null) {
            feedback.setVideoUrl("/api/v1/publics/product/feedbacks/video/" + feedback.getId());
            feedback = feedbackService.save(feedback);
            productFeedbackVideoStorageService.store(video, feedback.getId() + ".mp4");
        }

        if (images != null) {
            int count = 0;
            var services = (ProductFeedbackImageStorageService) productFeedbackImageStorageService;
            for (MultipartFile image : images) {
                count++;
                services.store(image, feedback.getId().toString(), count + ".jpg");
                var pFI = ProductFeedbackImage.builder()
                        .feedback(feedback)
                        .url("/api/v1/publics/product/feedbacks/image/" + feedback.getId() + "?imgId=" + count)
                        .build();
                productFeedbackImageService.save(pFI);
            }
        }

        orderDetailsService.setFeedbacked(orderDetails);
        Notification notification = Notification.builder()
                .title("Your shop has just received new feedback!")
                .content(String.format("Customer %s has just commented on the product of your shop. Please be quick to check and respond.", user.getFirstname()))
                .imageUrl(product.getImages().get(0).getUrl())
                .redirectUrl("/seller/portal/feedback")
                .shop(product.getShop())
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/reports")
    public ResponseEntity<String> uploadProductReport(
            @RequestParam("feedback") String jsonRequest,
            @RequestParam("images") Optional<MultipartFile[]> imgs,
            @RequestParam("video") Optional<MultipartFile> vd
    ) {
        var images = imgs.orElse(null);
        var video = vd.orElse(null);

        var request = gsonUtils.fromJson(jsonRequest, FeedbackRequest.class);
        var order = orderService.getById(request.getOrderId());
        var product = productService.getProductById(request.getProductId());
        var user = (User) authenticatedManager.getAuthenticatedUser();
        var orderDetails = orderDetailsService.getByOrderAndProduct(order, product);

        var feedbackRequest = Feedback.builder()
                .type("REPORT - " + request.getReason())
                .time(request.getTime())
                .description(request.getDescription())
                .product(product)
                .orderId(order.getId())
                .user(user)
                .build();
        Feedback feedback = feedbackService.saveSpecial(feedbackRequest);

        if (video != null) {
            feedback.setVideoUrl("/api/v1/publics/product/feedbacks/video/" + feedback.getId());
            feedback = feedbackService.saveSpecial(feedback);
            productFeedbackVideoStorageService.store(video, feedback.getId() + ".mp4");
        }

        if (images != null) {
            int count = 0;
            var services = (ProductFeedbackImageStorageService) productFeedbackImageStorageService;
            for (MultipartFile image : images) {
                count++;
                services.store(image, feedback.getId().toString(), count + ".jpg");
                var pFI = ProductFeedbackImage.builder()
                        .feedback(feedback)
                        .url("/api/v1/publics/product/feedbacks/image/" + feedback.getId() + "?imgId=" + count)
                        .build();
                productFeedbackImageService.save(pFI);
            }
        }

        orderDetailsService.setFeedbacked(orderDetails);
        Notification notification = Notification.builder()
                .title("Your shop has just received new report!")
                .content(String.format("Customer %s has just commented on the product of your shop. Please be quick to check and respond.", user.getFirstname()))
                .imageUrl(product.getImages().get(0).getUrl())
                .redirectUrl("/seller/portal/feedback")
                .shop(product.getShop())
                .createdAt(new Date())
                .read(false)
                .build();
        notificationService.save(notification);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/product/report/{id}")
    public ResponseEntity<String> sendProductReport(@RequestBody ReportRequest request) {
        Product product = productService.getProductById(request.getProductId());
        User user = (User) userService.getById(request.getReporterId());
        if (product == null || user == null) {
            return ResponseEntity.badRequest().build();
        }

        Report findReport = reportService.getByReporterAndProduct(user, product);
        if (findReport != null) return ResponseEntity.badRequest().build();

        Report report = Report.builder()
                .reporter(user)
                .product(product)
                .reasonType(request.getReasonType())
                .reasonSpecific(request.getReasonSpecific())
                .action("UNPROCESS")
                .build();

        reportService.save(report);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shop/report/{id}")
    public ResponseEntity<String> sendShopReport(@RequestBody ReportRequest request) {
        Shop shop = shopService.getShopById(request.getShopId());
        User user = (User) userService.getById(request.getReporterId());
        if (shop == null || user == null) {
            return ResponseEntity.badRequest().build();
        }

        Report findReport = reportService.getByReporterAndShop(user, shop);
        if (findReport != null) return ResponseEntity.badRequest().build();

        Report report = Report.builder()
                .reporter(user)
                .shop(shop)
                .reasonType(request.getReasonType())
                .reasonSpecific(request.getReasonSpecific())
                .action("UNPROCESS")
                .build();

        reportService.save(report);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/report/{id}")
    public ResponseEntity<String> sendOrderReport(@RequestBody ReportRequest request) {
        Order order = orderService.getById(request.getOrderId());
        User user = (User) userService.getById(request.getReporterId());

        if (order == null || user == null) {
            return ResponseEntity.badRequest().build();
        }

        Report findReport = reportService.getByReporterAndOrder(user, order);
        if (findReport != null) return ResponseEntity.badRequest().build();

        Report report = Report.builder()
                .reporter(user)
                .order(order)
                .reasonType(request.getReasonType())
                .reasonSpecific(request.getReasonSpecific())
                .action("UNPROCESS")
                .build();

        order.setReported(true);

        orderService.save(order);
        reportService.save(report);
        return ResponseEntity.ok().build();
    }
}
