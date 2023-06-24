/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.user;

import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.cart.CartItem;
import com.swp391.backend.model.cart.CartService;
import com.swp391.backend.model.cartProduct.CartProduct;
import com.swp391.backend.model.cartProduct.CartProductDTO;
import com.swp391.backend.model.cartProduct.CartProductKey;
import com.swp391.backend.model.cartProduct.CartProductService;
import com.swp391.backend.model.district.District;
import com.swp391.backend.model.district.DistrictService;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.notification.NotificationService;
import com.swp391.backend.model.notification.NotificationType;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.order.OrderStatus;
import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.orderDetails.OrderDetailsId;
import com.swp391.backend.model.orderDetails.OrderDetailsService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.province.ProvinceService;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.receiveinfo.ReceiveInfoService;
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
import com.swp391.backend.utils.storage.StorageService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    @Qualifier(value = "avatar")
    private StorageService storageService;
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
        if (shops.size() > 0)
        {
            shop = shops.get(0).toDto();
        }

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .imageurl(user.getImageurl())
                .gender(user.getGender())
                .shopDTO(shop)
                .receiveInfoPage(page)
                .defaultReceiveInfo(defaultInfo)
                .cartProducts(cartProducts)
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
    public ResponseEntity<Subscription> subscribeShop(@PathVariable("shop_id") Integer shopId)
    {
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
                .build();
        subscription = subscriptionService.save(subscription);

        Notification notification = Notification.builder()
                .title("Your shop just gained a new following")
                .content(String.format("Customer %s just followed your shop. Congratulations on your new follower!", user.getFirstname()))
                .imageUrl(user.getImageurl())
                .type(NotificationType.SUBSCRIPTION)
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

                        if (pSale != null)
                        {
                            dto = CartProductDTO.builder()
                                    .saleQuantity(pSale.getSaleQuantity())
                                    .saleSold(pSale.getSold())
                                    .product(i.getProduct())
                                    .salePercent(pSale.getSaleEvent().getPercent())
                                    .quantity(i.getQuantity())
                                    .build();
                        }else
                        {
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

                        if (pSale != null)
                        {
                            dto = CartProductDTO.builder()
                                    .saleQuantity(pSale.getSaleQuantity())
                                    .saleSold(pSale.getSold())
                                    .product(i.getProduct())
                                    .salePercent(pSale.getSaleEvent().getPercent())
                                    .quantity(i.getQuantity())
                                    .build();
                        }else
                        {
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
        CartProduct findedCartProduct = cartProductService.findByCartAndProduct(cart, product);

        if (findedCartProduct != null) {
            quantity += findedCartProduct.getQuantity();
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

    public String generateOrderId(ShopDTO shopDTO) {
        Shop shop = shopService.getShopById(shopDTO.getId());
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonthValue());
        String date = String.valueOf(LocalDateTime.now().getDayOfMonth());
        int numberOfOrder = orderService.getNumberOfOrderInCurrentDay(shop);
        return year + month + date + (numberOfOrder + 1);
    }

    @PostMapping("/order/create")
    public void createOrder(@RequestBody List<CheckOutRequest> checkOutRequests) {

        User user = (User) authenticatedManager.getAuthenticatedUser();
        Cart cart = user.getCart();
        checkOutRequests.forEach(it -> {

            Shop shop = shopService.getShopById(it.getShopId());
            Product product = productService.getProductById(it.getCheckOutItems().get(0).getProductId());
            String description = product.getDescription().substring(0, 150);

            Order order = Order.builder()
                    .id(generateOrderId(shop.toDto()))
                    .user(user)
                    .status(OrderStatus.PENDING)
                    .payment("COD")
                    .description(description)
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
                product1.setAvailable(product1.getAvailable() - 1);
                productService.save(product1);

                CartProductKey cpi = new CartProductKey();
                cpi.cartId = cart.getId();
                cpi.productId = product1.getId();

                cartProductService.delete(cpi);
            });
            order.setSellPrice(order.getSellPrice() + 20);
        });
    }

    @PostMapping("/order/special/create")
    public void createSpecialOrder(@RequestBody Integer productId, @RequestParam("quantity") Optional<Integer> quan)
    {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        Product product = productService.getProductById(productId);
        if (product.getCategoryGroup().getCategory().getId() != 1)
        {

        }

        Shop shop = shopService.getShopById(product.getShop().getId());
        Integer quantity = quan.orElse(1);

        Order order = Order.builder()
                .id(generateOrderId(shop.toDto()))
                .user(user)
                .status(OrderStatus.SPECIAL_SHOP)
                .payment("COD")
                .description(product.getDescription().substring(0, 150))
                .shop(shop)
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

        product.setAvailable(product.getAvailable() - 1);
        productService.save(product);
    }

    @PostMapping("/order/special/process/{id}")
    public ResponseEntity<String> processSpecialOrder(
            @PathVariable("id") String orderId,
            @RequestParam("action") String action
    ) {

        Order order = orderService.getById(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }

        if (action.equals("CONFIRM"))
        {
            order.setStatus(OrderStatus.SHIPPING);
        }

        if (action.equals("REJECT"))
        {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderService.save(order);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    public List<Notification> getNotification()
    {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        return user.getNotifications();
    }


}
