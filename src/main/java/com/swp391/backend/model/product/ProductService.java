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
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.utils.storage.ProductImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

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

    public List<Product> getAllProduct() {
        return productRepository.findAll(Sort.by("sold").descending());
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

    public int getMaxPage(Shop shop) {
        List<Product> list = productRepository.findByShop(shop);
        int length = list.size();
        int page = Math.floorDiv(length, 40) + 1;
        return page;
    }

    public int getMaxPage(String search) {
        List<Product> list = productRepository.findByNameContainingIgnoreCase(search);
        int length = list.size();
        int page = Math.floorDiv(length, 40) + 1;
        return page;
    }

    public int getMaxPage(Category category) {
        List<Product> list = productRepository.findByCategory(category.getId());
        int length = list.size();
        int page = Math.floorDiv(length, 40) + 1;
        return page;
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
        return productRepository.findByNameContainingIgnoreCase(search, pageable);
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
        return productRepository.findByCategory(category.getId(), pageable);
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
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

        for (int i = 1; i < 20; i++) {
            double price = Math.round(Math.random() * 999 + 1000);
            var product1 = Product.builder()
                    .name("African crested bird")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(1))
                    .shop(shopService.getShopById(1))
                    .video("/api/v1/publics/product/video/" + 1)
                    .uploadTime(new Date())
                    .description(des)
                    .available(20)
                    .price(price)
                    .build();
            save(product1);

            for (int j = 1; j <= 4; j++) {
                var pI = ProductImage.builder()
                        .product(product1)
                        .url("/api/v1/publics/product/image/" + i + "?imgId=" + j)
                        .build();
                productImageService.save(pI);
            }


            List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(product1.getCategoryGroup().getCategory());
            categoryDetailInfos.forEach(it -> {
                String value = "";
                if (it.getName().equals("Age")) {
                    value = "More than 1 year";
                } else if (it.getName().equals("Weight")) {
                    value = "1kg";
                } else if (it.getName().equals("Brand")) {
                    value = shopService.getShopById(1).getName();
                } else {
                    value = "Natural";
                }

                var pdi = ProductDetailInfo.builder()
                        .categoryDetailInfo(it)
                        .value(value)
                        .product(product1)
                        .build();
                productDetailInfoService.save(pdi);
            });

            var feedback1 = Feedback.builder()
                    .rate(4)
                    .time(new Date())
                    .description("Pretty Fine")
                    .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                    .product(product1)
                    .user((User) userService.loadUserByUsername("vuducthien@gmail.com"))
                    .build();

            feedbackService.save(feedback1);

            var pfi = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=1")
                    .feedback(feedback1)
                    .build();
            var pfi2 = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=2")
                    .feedback(feedback1)
                    .build();
            productFeedbackImageService.save(pfi);
            productFeedbackImageService.save(pfi2);

            var feedbackRep = FeedbackReply.builder()
                    .feedback(feedback1)
                    .content("We would like to thank our users for their continued support of our product. We are always working to improve the product, and we appreciate your feedback. We know that there are still some features that are missing, and we are sorry for any inconvenience this may cause. We are working hard to add these features as soon as possible.")
                    .build();

            feedbackReplyService.save(feedbackRep);

            var feedback2 = Feedback.builder()
                    .rate(4)
                    .time(new Date())
                    .description("Pretty Fine")
                    .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                    .product(product1)
                    .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                    .build();

            feedbackService.save(feedback2);

            var pfi1 = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=1")
                    .feedback(feedback2)
                    .build();
            var pfi22 = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=2")
                    .feedback(feedback2)
                    .build();
            productFeedbackImageService.save(pfi1);
            productFeedbackImageService.save(pfi22);

            var feedbackRep2 = FeedbackReply.builder()
                    .feedback(feedback2)
                    .content("We would like to thank our users for their continued support of our product. We are always working to improve the product, and we appreciate your feedback. We know that there are still some features that are missing, and we are sorry for any inconvenience this may cause. We are working hard to add these features as soon as possible.")
                    .build();

            feedbackReplyService.save(feedbackRep2);
        }
        for (int i = 20; i < 40; i++) {
            double price = Math.round(Math.random() * 999 + 1000);
            var product1 = Product.builder()
                    .name("African bird's food")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(3))
                    .shop(shopService.getShopById(2))
                    .video("/api/v1/publics/product/video/" + 1)
                    .uploadTime(new Date())
                    .description(des)
                    .available(20)
                    .price(price)
                    .build();
            save(product1);

            for (int j = 1; j <= 4; j++) {
                var pI = ProductImage.builder()
                        .product(product1)
                        .url("/api/v1/publics/product/image/" + i + "?imgId=" + j)
                        .build();
                productImageService.save(pI);
            }


            List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(product1.getCategoryGroup().getCategory());
            categoryDetailInfos.forEach(it -> {
                String value = "";
                if (it.getName().equals("Age")) {
                    value = "More than 1 year";
                } else if (it.getName().equals("Weight")) {
                    value = "1kg";
                } else if (it.getName().equals("Brand")) {
                    value = shopService.getShopById(2).getName();
                } else {
                    value = "Natural";
                }

                var pdi = ProductDetailInfo.builder()
                        .categoryDetailInfo(it)
                        .value(value)
                        .product(product1)
                        .build();
                productDetailInfoService.save(pdi);
            });

            var feedback1 = Feedback.builder()
                    .rate(4)
                    .time(new Date())
                    .description("Pretty Fine")
                    .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                    .product(product1)
                    .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                    .build();

            feedbackService.save(feedback1);

            var pfi = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=1")
                    .feedback(feedback1)
                    .build();
            var pfi2 = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=2")
                    .feedback(feedback1)
                    .build();
            productFeedbackImageService.save(pfi);
            productFeedbackImageService.save(pfi2);
        }
        for (int i = 40; i < 60; i++) {
            double price = Math.round(Math.random() * 999 + 1000);
            var product1 = Product.builder()
                    .name("African bird's cage")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(4))
                    .shop(shopService.getShopById(3))
                    .uploadTime(new Date())
                    .description(des)
                    .available(20)
                    .price(price)
                    .build();
            save(product1);

            for (int j = 1; j <= 4; j++) {
                var pI = ProductImage.builder()
                        .product(product1)
                        .url("/api/v1/publics/product/image/" + i + "?imgId=" + j)
                        .build();
                productImageService.save(pI);
            }


            List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(product1.getCategoryGroup().getCategory());
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
                        .product(product1)
                        .build();
                productDetailInfoService.save(pdi);
            });

            var feedback1 = Feedback.builder()
                    .rate(4)
                    .time(new Date())
                    .description("Pretty Fine")
                    .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                    .product(product1)
                    .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                    .build();

            feedbackService.save(feedback1);

            var pfi = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=1")
                    .feedback(feedback1)
                    .build();
            var pfi2 = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + 1 + "?imgId=2")
                    .feedback(feedback1)
                    .build();
            productFeedbackImageService.save(pfi);
            productFeedbackImageService.save(pfi2);
        }
        for (int i = 60; i < 80; i++) {
            double price = Math.round(Math.random() * 999 + 1000);
            var product1 = Product.builder()
                    .name("African bird's accessories")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(4))
                    .shop(shopService.getShopById(3))
                    .uploadTime(new Date())
                    .description(des)
                    .available(20)
                    .price(price)
                    .build();
            save(product1);

            for (int j = 1; j <= 4; j++) {
                var pI = ProductImage.builder()
                        .product(product1)
                        .url("/api/v1/publics/product/image/" + i + "?imgId=" + j)
                        .build();
                productImageService.save(pI);
            }


            List<CategoryDetailInfo> categoryDetailInfos = categoryDetailInfoService.getByCategory(product1.getCategoryGroup().getCategory());
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
                        .product(product1)
                        .build();
                productDetailInfoService.save(pdi);
            });

            Feedback feedback1 = null;
            float rd = Math.round(Math.random());
            if (rd == 0) {
                feedback1 = Feedback.builder()
                        .rate(4)
                        .time(new Date())
                        .description("Pretty Fine")
                        .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                        .product(product1)
                        .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                        .build();
            } else {
                feedback1 = Feedback.builder()
                        .rate(1)
                        .time(new Date())
                        .description("So Bad")
                        .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                        .product(product1)
                        .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                        .build();
            }
            feedbackService.save(feedback1);

            var pfi = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + feedback1.getId() + "?imgId=1")
                    .feedback(feedback1)
                    .build();
            var pfi2 = ProductFeedbackImage.builder()
                    .url("/api/v1/publics/product/feedbacks/image/" + feedback1.getId() + "?imgId=2")
                    .feedback(feedback1)
                    .build();
            productFeedbackImageService.save(pfi);
            productFeedbackImageService.save(pfi2);
        }
    }
}
