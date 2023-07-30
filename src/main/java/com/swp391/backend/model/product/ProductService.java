package com.swp391.backend.model.product;

import com.swp391.backend.model.category.Category;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfo;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfoService;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImageService;
import com.swp391.backend.model.productFeedbackReply.FeedbackReply;
import com.swp391.backend.model.productFeedbackReply.FeedbackReplyService;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productImage.ProductImageServie;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryGroupService categoryGroupService;
    private final ShopService shopService;
    private final UserService userService;
    private final FeedbackService feedbackService;
    private final ProductFeedbackImageService productFeedbackImageService;
    private final ProductDetailInfoService productDetailInfoService;
    private final CategoryDetailInfoService categoryDetailInfoService;
    private final ProductImageServie productImageService;
    private final FeedbackReplyService feedbackReplyService;
    private final SettingService settingService;

    public List<Product> getAllProduct() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        return productRepository.findAll(Sort.by("sold").descending());
    }

    public List<Product> getAllProductForAdmin() {
        Pageable pageable = PageRequest.of(0, 666, Sort.by("uploadTime").descending());
        return productRepository.findAll(pageable).getContent();
    }

    public Integer getTotalProduct() {
        return productRepository.findAll().size();
    }

    public List<Product> getSoldTopTenProduct() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        return productRepository.findAll(pageable).getContent();
    }

    public List<Product> getByShop(Shop shop, Integer page, String filter) {
        Pageable pageable = null;
        switch (filter) {
            case "top sales":
                pageable = PageRequest.of(page, 40, Sort.by("sold").descending());
                break;
            case "ratings":
                pageable = PageRequest.of(page, 40, Sort.by("rating").descending());
                break;
            case "low to high":
                pageable = PageRequest.of(page, 40, Sort.by("price").ascending());
                break;
            case "high to low":
                pageable = PageRequest.of(page, 40, Sort.by("price").descending());
                break;
            default:
                pageable = PageRequest.of(page, 40, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        }
        return productRepository.findByShop(shop, pageable);
    }

    public List<Product> getByOwnShop(Shop shop, Integer page, String filter) {
        Pageable pageable = null;
        switch (filter) {
            case "name.a-z":
                pageable = PageRequest.of(page, 5, Sort.by("name").ascending());
                break;
            case "name.z-a":
                pageable = PageRequest.of(page, 5, Sort.by("name").descending());
                break;
            case "price.h-l":
                pageable = PageRequest.of(page, 5, Sort.by("price").descending());
                break;
            case "price.l-h":
                pageable = PageRequest.of(page, 5, Sort.by("price").ascending());
                break;
            case "quantity.asc":
                pageable = PageRequest.of(page, 5, Sort.by("available").ascending());
                break;
            case "quantity.desc":
                pageable = PageRequest.of(page, 5, Sort.by("available").descending());
                break;
            default:
                pageable = PageRequest.of(page, 5, Sort.by("uploadTime").descending());
        }
        return productRepository.findByShop(shop, pageable);
    }

    public List<Product> getByOwnShopAndBan(Shop shop, Integer page, String filter) {
        Pageable pageable = null;
        switch (filter) {
            case "name.a-z":
                pageable = PageRequest.of(page, 5, Sort.by("name").ascending());
                break;
            case "name.z-a":
                pageable = PageRequest.of(page, 5, Sort.by("name").descending());
                break;
            case "price.h-l":
                pageable = PageRequest.of(page, 5, Sort.by("price").descending());
                break;
            case "price.l-h":
                pageable = PageRequest.of(page, 5, Sort.by("price").ascending());
                break;
            case "quantity.asc":
                pageable = PageRequest.of(page, 5, Sort.by("available").ascending());
                break;
            case "quantity.desc":
                pageable = PageRequest.of(page, 5, Sort.by("available").descending());
                break;
            default:
                pageable = PageRequest.of(page, 5, Sort.by("uploadTime").descending());
        }
        return productRepository.findByShopAndBan(shop, true, pageable);
    }

    public int ceil(float num)
    {
        int rounded = Math.round(num);
        if (rounded < num)
        {
            rounded += 1;
        }
        return rounded;
    }

    public int getMaxPage(Shop shop) {
        List<Product> list = productRepository.findByShop(shop);
        int length = list.size();
        float div = length * 1.0f / 40;
        return ceil(div);
    }

    public int getMaxPageOwnShop(Shop shop) {
        List<Product> list = productRepository.findByShop(shop);
        int length = list.size();
        float div = length * 1.0f / 5;
        return ceil(div);
    }

    public int getMaxPage(String search) {
        List<Product> list = productRepository.findByNameContainingIgnoreCaseAndBan(search, false);
        int length = list.size();
        float div = length * 1.0f / 40;
        return ceil(div);
    }

    public int getMaxPage(Category category) {
        List<Product> list = productRepository.findByCategory(category);
        int length = list.size();
        float div = length * 1.0f / 40;
        return ceil(div);
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> searchProduct(String search, Integer page, String filter) {
        Pageable pageable = null;
        switch (filter) {
            case "top sales":
                pageable = PageRequest.of(page, 40, Sort.by("sold").descending());
                break;
            case "ratings":
                pageable = PageRequest.of(page, 40, Sort.by("rating").descending());
                break;
            case "low to high":
                pageable = PageRequest.of(page, 40, Sort.by("price").ascending());
                break;
            case "high to low":
                pageable = PageRequest.of(page, 40, Sort.by("price").descending());
                break;
            default:
                pageable = PageRequest.of(page, 40, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        }
        return productRepository.findByNameContainingIgnoreCaseAndBan(search, false, pageable);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getByCategory(Category category, Integer page, String filter, Integer numOfItem) {
        Pageable pageable = null;
        switch (filter) {
            case "top sales":
                pageable = PageRequest.of(page, numOfItem, Sort.by("sold").descending());
                break;
            case "ratings":
                pageable = PageRequest.of(page, numOfItem, Sort.by("rating").descending());
                break;
            case "low to high":
                pageable = PageRequest.of(page, numOfItem, Sort.by("price").ascending());
                break;
            case "high to low":
                pageable = PageRequest.of(page, numOfItem, Sort.by("price").descending());
                break;
            default:
                pageable = PageRequest.of(page, numOfItem, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        }
        return productRepository.findByCategory(category, pageable);
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public void initor(String name, String des, int order, int category, int shopId)
    {
        double price = Math.round(Math.random() * 100) + 1;
        var product = Product.builder()
                .name(name)
                .categoryGroup(categoryGroupService.getCategoryGroupById(category))
                .shop(shopService.getShopById(shopId))
                .video("/api/v1/publics/product/video/" + 3)
                .uploadTime(new Date())
                .description(des)
                .available(1)
                .price(price)
                .build();
        save(product);

        for (int j = 1; j <= 4; j++) {
            var pI = ProductImage.builder()
                    .product(product)
                    .url("/api/v1/publics/product/image/" + order + "?imgId=" + j)
                    .build();
            productImageService.save(pI);
        }


        List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(product.getCategoryGroup().getCategory());
        categoryDetailInfos.forEach(it -> {
            String value = "";
            if (it.getName().equals("Age")) {
                value = "More than 1 year";
            } else if (it.getName().equals("Weight")) {
                value = "1kg";
            } else if (it.getName().equals("Brand")) {
                value = shopService.getShopById(3).getName();
            } else {
                value = "Natural";
            }

            var pdi = ProductDetailInfo.builder()
                    .categoryDetailInfo(it)
                    .value(value)
                    .product(product)
                    .build();
            productDetailInfoService.save(pdi);
        });

        Feedback feedback = null;
        float rd = Math.round(Math.random());
        if (rd == 0) {
            feedback = Feedback.builder()
                    .rate(4)
                    .time(new Date())
                    .type("RATE PRODUCT")
                    .description("Pretty Fine")
                    .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                    .product(product)
                    .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                    .build();
        } else {
            feedback = Feedback.builder()
                    .rate(1)
                    .time(new Date())
                    .type("RATE PRODUCT")
                    .description("So Bad")
                    .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                    .product(product)
                    .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                    .build();
        }
        feedbackService.save(feedback);

        var pfi = ProductFeedbackImage.builder()
                .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=1")
                .feedback(feedback)
                .build();
        var pfi2 = ProductFeedbackImage.builder()
                .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=2")
                .feedback(feedback)
                .build();
        productFeedbackImageService.save(pfi);
        productFeedbackImageService.save(pfi2);
    }

    public void init() {
        String des = "\uD83D\uDC69 PRODUCT DESCRIPTION\n" +
                "\n" +
                "   - Oversize men's and women's t-shirts using 4-way stretch cotton 65/35 fabric. This is a fabric with characteristics of softness, high elasticity, good absorbency and is completely woven from natural cotton fibers. Cotton fabric is extremely skin-friendly.\n" +
                "  \n" +
                "   - Comfortable round neck men's and women's T-shirts\n" +
                "  \n" +
                "   - Wide form unisex t-shirt, easy to coordinate. You can combine it with jeans, skirts, ... combined with a pair of sneakers for you to confidently go down the street\n" +
                "  \n" +
                "  \n" +
                "  \n" +
                "   \uD83D\uDCE3 INSTRUCTIONS FOR STORAGE OF OVERSIZE VENDER Shop T-shirts\n" +
                "\n" +
                "   - Turn men's and women's short-sleeved t-shirts inside out when washing, do not wash white unisex t-shirts with dark clothes.\n" +
                "  \n" +
                "   - Use neutral soap, do not use soap with strong detergents for oversized men's and women's t-shirts.\n" +
                "  \n" +
                "   - Do not use bleach, do not soak unisex t-shirts.\n" +
                "  \n" +
                "   - Hang horizontally, do not hang when the unisex t-shirt is wet, do not dry in direct sunlight.\n" +
                "  \n" +
                "  \n" +
                "  \n" +
                "   \uFE0F\uD83C\uDFAF Delivery of the correct size, 1 to 1 exchange error\n" +
                "  \n" +
                "   \uFE0F\uD83C\uDFAF Delivery of COD nationwide\n" +
                "  \n" +
                "   \uFE0F\uD83C\uDFAF Support to change size and change color within 7 days from the date of receipt of the goods, the exchange product must still have the original mac stamp and have not been used.\n" +
                "  \n" +
                "   ⚠\uFE0F NOTE: When opening the product, please return to the video of the product opening process to be 100% guaranteed to exchange for a new product if the VENDER T-shirt delivered is faulty.";


        initor("Vietnamese Greenfinch", des, 1, 3,1);
        initor("Collared Laughingthrush", des, 2,3, 1);
        initor("Grey-crowned Crocias", des, 3, 3, 1);
        initor("Indochinese Green Magpie", des, 4, 3, 1);
        initor("Aegithina tiphia", des, 5, 3, 1);
        initor("Alophoixus pallidus", des, 6, 3, 1);
        initor("Aegithina lafresnayei", des, 7, 3, 1);
        initor("Pycnonotus jocosus", des, 8, 3, 1);
        initor("Streptopelia chinensis", des, 9, 3, 1);
        initor("Pnoepyga immaculata", des, 10, 3, 1);
        initor("Columba livia", des, 11, 3, 1);
        initor("Acridotheres tristis", des, 12, 3, 1);
        initor("Lonchura domestica", des, 13, 3, 1);
        initor("Amandava amandava", des, 14, 3, 1);
        initor("Turdus philomelos", des, 15, 3, 1);

        initor("Nectar for the Gods", des, 16, 6,2);
        initor("Suet Cakes", des, 17,6, 2);
        initor("Black Oil Sunflower Seeds", des, 18, 6, 2);
        initor("Nyjer Seed", des, 19, 6, 2);
        initor("Cracked Corn", des, 20, 6, 2);
        initor("Peanuts", des, 21, 6, 2);
        initor("Millet", des, 22, 6, 2);
        initor("Fruit Blend", des, 23, 6, 2);
        initor("Mealworms", des, 24, 6, 2);
        initor("Suet Balls", des, 25, 6, 2);
        initor("Winter Blend", des, 26, 6, 2);
        initor("Annual Blend", des, 27, 6, 2);
        initor("No-Waste Seed Mix", des, 28, 6, 2);
        initor("Hummingbird Feeder", des, 29, 6, 2);
        initor("Bird Bath", des, 30, 6, 2);

        initor("Shanghai Parakeet Cage", des, 31, 9, 3);
        initor("Beach Dome Top Bird Cage", des, 32, 9, 3);
        initor("Bird Travel Carrier Cage", des, 33, 9, 3);
        initor("PawHut Bird Cage", des, 34, 9, 3);
        initor("Outdoor Bird Cage", des, 35, 9, 3);
        initor("WOODEN BIRD CAGE", des, 36, 9, 3);
        initor("Wooden Bird Cage vintage", des, 37, 9, 3);
        initor("Chinese Hexagons Bird Cage", des, 38, 9, 3);
        initor("Bamboo Carving Bird Cage", des, 39, 9, 3);
        initor("Natural Bamboo Bird Cage", des, 40, 9, 3);
        initor("Playtop Parrot Bird Cage", des, 41, 9, 3);
        initor("Vision Bird Cage", des, 42, 9, 3);
        initor("Segawe Bird Cage", des, 43, 9, 3);
        initor("Metal Hexagon Birdcage", des, 44, 9, 3);
        initor("Wire Bird Cage", des, 45, 9,3);

        initor("Kaytee Foraging Toys", des, 46, 13, 3);
        initor("BWOGUE 5-Piece Bird Toy Set", des, 47, 13, 3);
        initor("SunGrow Chewing Toy", des, 48, 13, 3);
        initor("Bonka Bird Toys 1969 Spoon Delight", des, 49, 13, 3);
        initor("Prevue Hendryx Rope Ladder", des, 50, 13, 3);
        initor("Niteangel Natural Living Playground", des, 51, 13, 3);
        initor("Yaheetech Bird Ladder", des, 52, 13, 3);
        initor("Paradise Parrot Toys Hanging Bell", des, 53, 13, 3);
        initor("KAYTEE Foraging Snack Ball", des, 54, 13, 3);
        initor("PetFusion Hanging Macaw Toy", des, 55, 13, 3);
        initor("Wood Swing Bird Chew Toy", des, 56, 13, 3);
        initor("Stainless Steel Bird Bell", des, 57, 13, 3);
        initor("Bird Kabob Chiquito Chew Toy", des, 58, 13, 3);
        initor("Cuttlebone", des, 59, 13, 3);
        initor("Bird Mirror", des, 60, 13, 3);
    }
}
