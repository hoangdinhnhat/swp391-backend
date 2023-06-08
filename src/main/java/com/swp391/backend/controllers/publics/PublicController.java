/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.controllers.publics;

import java.util.*;
import java.util.stream.Collectors;

import com.swp391.backend.model.cart.Cart;
import com.swp391.backend.model.cart.CartService;
import com.swp391.backend.model.cartProduct.CartProduct;
import com.swp391.backend.model.cartProduct.CartProductKey;
import com.swp391.backend.model.cartProduct.CartProductService;
import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.category.CategoryRepository;
import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoRepository;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductDTO;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.product.SearchDTO;
import com.swp391.backend.model.productAttachWith.AttachWithService;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfoRepository;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImageService;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleDTO;
import com.swp391.backend.model.productSale.ProductSaleKey;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.saleEvent.SaleEvent;
import com.swp391.backend.model.saleEvent.SaleEventService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopDetails;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.utils.json.JsonUtils;
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
    private final CategoryRepository categoryRepository;
    private final CategoryDetailInfoRepository categoryDetailInfoRepository;
    private final ProductDetailInfoRepository productDetailInfoRepository;
    private final SaleEventService saleEventService;
    private final ProductSaleService productSaleService;
    private final JsonUtils JSON;
    private final FeedbackService feedbackService;
    private final ProductFeedbackImageService productFeedbackImageService;
    private final CategoryDetailInfoService categoryDetailInfoService;

    @Autowired
    @Qualifier(value = "avatar")
    private StorageService storageService;
    @Autowired
    @Qualifier(value = "productImage")
    private StorageService productImageService;

    @Autowired
    @Qualifier(value = "productVideo")
    private StorageService productVideoService;

    @Autowired
    @Qualifier(value = "productFeedbackVideo")
    private StorageService productFeedbackVideoStorageService;

    @Autowired
    @Qualifier(value = "productFeedbackImage")
    private StorageService productFeedbackImageStorageService;

//    @GetMapping
//    public Product init() {
//
//    }

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
    public ResponseEntity<SearchDTO> search(@RequestParam("keyword") String keyword, @RequestParam("page") Optional<Integer> pageOpt) {
        Integer page = pageOpt.orElse(1) - 1;
        Shop shop = shopService.searchShop(keyword);
        ProductSaleDTO productSaleDto = null;
        List<ProductSaleDTO> products = productService.searchProduct(keyword, page).stream()
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

        var searchDTO = SearchDTO.builder()
                .shop(dto)
                .products(products)
                .build();
        return ResponseEntity.ok().body(searchDTO);
    }

    private final CategoryService categoryService;

    @GetMapping("/category/{category_id}")
    public List<ProductDTO> search(@PathVariable("category_id") Integer category_id) {
        Category category = categoryService.getById(category_id);
        List<Product> products = new ArrayList<>();
        category.getCategoryGroups().forEach(it -> {
            products.addAll(it.getProducts());
        });

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
                    .category(category.toDto())
                    .price(it.getPrice())
                    .rating(it.getRating())
                    .sold(it.getSold())
                    .productSale(productSaleDto)
                    .build();
        }).collect(Collectors.toList());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> productDetail(@PathVariable("id") Optional<Integer> id) {
        Integer pid = id.orElse(1);
        var p = productService.getProductById(pid);
        var cg = categoryGroupService.getCategoryGroupById(p.getCategoryGroup().getId());
        var shop = shopService.getShopById(p.getShop().getId());

        List<CategoryGroup> attachWith = attachWithService.getAttachWith(cg);
        List<Product> products = categoryGroupService.getProductByCategoryGroups(p, attachWith);

        var feedbacks = p.getFeedbacks().stream()
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
                .build();

        return ResponseEntity.ok().body(productDTO);
    }


    @PostMapping("/product/upload")
    public ResponseEntity<Product> uploadProduct(@RequestBody ProductRequest request) {
        CategoryGroup group = categoryGroupService.getCategoryGroupById(request.getCategoryGroup());
        var product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .available(request.getAvailable())
                .rating(request.getRating())
                .categoryGroup(group)
                .build();
        productService.save(product);
        return ResponseEntity.ok().body(product);
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
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<Resource> getProductImage(@PathVariable("id") Integer id, @RequestParam("imgId") Optional<Integer> imgId) {
        Product product = productService.getProductById(id);
        if (product != null) {
            String image = product.getId() + "/" + imgId.orElse(1) + ".jpg";
            Resource file = productImageService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "image/jpeg");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/product/image/{id}")
    public ResponseEntity<String> uploadProductImage(@PathVariable("id") Integer id, @RequestParam("files") MultipartFile[] files) {
        Product product = productService.getProductById(id);
        if (product != null) {
            var storage = (ProductImageStorageService) productImageService;
            Arrays.stream(files).forEach(file -> {
                int index = Arrays.stream(files).collect(Collectors.toList()).indexOf(file) + 1;
                storage.store(file, product.getId() + "", index + ".jpg");
            });
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/video/{id}")
    public ResponseEntity<Resource> getProductVideo(@PathVariable("id") Integer id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            String image = product.getId() + ".mp4";
            Resource file = productVideoService.loadAsResource(image);
            HttpHeaders header = new HttpHeaders();
            header.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"");
            header.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
            return ResponseEntity.ok().headers(header).body(file);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/product/video/{id}")
    public ResponseEntity<String> uploadProductVideo(@PathVariable("id") Integer id, @RequestParam("files") MultipartFile file) {
        Product product = productService.getProductById(id);
        if (product != null) {
            productVideoService.store(file, id + ".mp4");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/product/feedbacks/video/{feedback_id}")
    public ResponseEntity<Resource> getProductFeedbackVideo(@PathVariable("feedback_id") Integer feedbackId) {
        Feedback feedback = feedbackService.getById(feedbackId);
        if (feedback != null) {
            String video = feedback.getId() + "/" + feedback.getId() + ".mp4";
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
    public Shop shopDetail(@PathVariable("id") Optional<Integer> id) {
        Integer shopId = id.orElse(1);
        Shop shop = shopService.getShopById(shopId);
        return shop;
    }

    @GetMapping("/shop/trending")
    public List<ShopDTO> shopTrending() {
        return shopService.getShopTrending().stream().map(it -> it.toDto()).collect(Collectors.toList());
    }

    @GetMapping("/event/{sale_event}")
    public List<ProductSaleDTO> saleEventDetail(@PathVariable("sale_event") Integer sale_event, @RequestParam("page") Optional<Integer> page, @RequestParam("priority") Optional<Integer> priority) {
        SaleEvent event = saleEventService.getSaleEventById(sale_event);
        Integer pageId = page.orElse(1) - 1;
        Integer priorityId = priority.orElse(-1);
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
        return result;
    }

    private final CartProductService cartProductService;
    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/cart")
    public HashMap<String, List<CartProduct>> getCart() {
        User user = (User) userService.loadUserByUsername("motajil610@rockdian.com");
        Cart cart = user.getCart();
        HashMap<String, List<CartProduct>> mapper = new HashMap<>();
        cartProductService.getCartProductByCart(cart, 1)
                .forEach(i -> {
                    Integer shopId = i.getProduct().getShop().getId();
                    Shop shop = shopService.getShopById(shopId);
                    String shopJson = JSON.stringify(shop.toDto());
                    if (mapper.get(shopJson) == null) {
                        List<CartProduct> products = new ArrayList<>();
                        products.add(i);
                        mapper.put(shopJson, products);
                    } else {
                        List<CartProduct> products = mapper.get(shopJson);
                        products.add(i);
                        mapper.put(shopJson, products);
                    }
                });
        return mapper;
    }

    @GetMapping("/cart/{product_id}")
    public ResponseEntity<String> addProductToCart(@PathVariable("product_id") Integer product_id, @RequestParam("quantity") Optional<Integer> quan) {
        User user = (User) userService.loadUserByUsername("vuducthien@gmail.com");
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
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/cart/delete/{product_id}")
    public ResponseEntity<String> delete(@PathVariable("product_id") Integer product_id) {
        User user = (User) userService.loadUserByUsername("vuducthien@gmail.com");
        Cart cart = user.getCart();
        if (cart == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productService.getProductById(product_id);

        CartProductKey id = new CartProductKey();
        id.cartId = cart.getId();
        id.productId = product.getId();

        cartProductService.delete(id);
        return ResponseEntity.ok("OK");
    }

}
