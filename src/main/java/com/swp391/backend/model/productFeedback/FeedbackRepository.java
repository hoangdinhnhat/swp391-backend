package com.swp391.backend.model.productFeedback;

import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.shop.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FeedbackRepository extends PagingAndSortingRepository<Feedback, Integer>, JpaRepository<Feedback, Integer> {
    List<Feedback> findByProduct(Product product, Pageable pageable);

    List<Feedback> findByProductAndRate(Product product, Integer rate, Pageable pageable);

    @Query(
            "SELECT F " +
                    "FROM Feedback F " +
                    "JOIN Product P " +
                    "WHERE P.shop = ?1"
    )
    List<Feedback> findByShop(Shop shop, Pageable pageable);

    @Query(
            "SELECT F " +
                    "FROM Feedback F " +
                    "JOIN Product P " +
                    "WHERE P.shop = ?1 " +
                    "AND F.rate = ?2"
    )
    List<Feedback> findByShopAndRate(Shop shop, Integer rate, Pageable pageable);
}
