package com.swp391.backend.model.product;

import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfo;
import com.swp391.backend.model.categoryDetailInfo.CategoryDetailInfoService;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfo;
import com.swp391.backend.model.productDetailInfo.ProductDetailInfoService;
import com.swp391.backend.model.productFeedback.Feedback;
import com.swp391.backend.model.productFeedback.FeedbackService;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImage;
import com.swp391.backend.model.productFeedbackImage.ProductFeedbackImageService;
import com.swp391.backend.model.productImage.ProductImage;
import com.swp391.backend.model.productImage.ProductImageServie;
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
    private final PasswordEncoder passwordEncoder;
    private final FeedbackService feedbackService;
    private final ProductFeedbackImageService productFeedbackImageService;
    private final ProductDetailInfoService productDetailInfoService;
    private final CategoryDetailInfoService categoryDetailInfoService;
    private final ProductImageServie productImageService;

    public List<Product> getAllProduct() {
        return productRepository.findAll(Sort.by("sold").descending());
    }

    public List<Product> getSoldTopTenProduct()
    {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        return productRepository.findAll(pageable).getContent();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<Product> searchProduct(String search, Integer page) {
        Pageable pageable = PageRequest.of(page, 40, Sort.by("sold").descending().and(Sort.by("rating").descending()));
        return productRepository.findByNameContainingIgnoreCase(search, pageable);
    }

    public Product save(Product product) {
        return productRepository.save(product);
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
            var product1 = Product.builder()
                    .name("African crested bird")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(1))
                    .shop(shopService.getShopById(1))
                    .video("/api/v1/publics/product/video/" + 1)
                    .description(des)
                    .available(20)
                    .price(1000)
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
        }
        for (int i = 20; i < 40; i++) {
            var product1 = Product.builder()
                    .name("African bird's food")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(3))
                    .shop(shopService.getShopById(2))
                    .video("/api/v1/publics/product/video/" + 1)
                    .description(des)
                    .available(20)
                    .price(1000)
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
            var product1 = Product.builder()
                    .name("African bird's cage")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(4))
                    .shop(shopService.getShopById(3))
                    .description(des)
                    .available(20)
                    .price(1000)
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
            var product1 = Product.builder()
                    .name("African bird's accessories")
                    .categoryGroup(categoryGroupService.getCategoryGroupById(4))
                    .shop(shopService.getShopById(3))
                    .description(des)
                    .available(20)
                    .price(1000)
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
            if(rd == 0)
            {
                feedback1 = Feedback.builder()
                        .rate(4)
                        .time(new Date())
                        .description("Pretty Fine")
                        .videoUrl("/api/v1/publics/product/feedbacks/video/1")
                        .product(product1)
                        .user((User) userService.loadUserByUsername("tranthienthanhbao@gmail.com"))
                        .build();
            }else
            {
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
