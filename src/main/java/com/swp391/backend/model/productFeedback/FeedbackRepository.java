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
                    "INNER JOIN Product P " +
                    "ON F.product = P " +
                    "WHERE P.shop = ?1"
    )
    List<Feedback> findByShop(Shop shop, Pageable pageable);

    @Query(
            "SELECT F " +
                    "FROM Feedback F " +
                    "INNER JOIN Product P " +
                    "ON F.product = P " +
                    "WHERE P.shop = ?1 " +
                    "AND F.rate = ?2"
    )
    List<Feedback> findByShopAndRate(Shop shop, Integer rate, Pageable pageable);

    @Query(
            value = "SELECT COUNT(f.id) " +
                    "FROM shop s " +
                    "INNER JOIN product p ON p.shop_id = s.id " +
                    "INNER JOIN feedback f ON f.product_id = p.id " +
                    "WHERE s.id = ?1 " +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(f.time) " +
                    "  AND EXTRACT(HOUR FROM f.time) >= ?2 " +
                    "  AND EXTRACT(HOUR FROM f.time) <= ?3",
            nativeQuery = true)
    Integer getFeedbackAnalystByHourRange(Integer id, int prev, int i);

    @Query(
            value = "SELECT COUNT(f.id) " +
                    "FROM shop s " +
                    "INNER JOIN product p ON p.shop_id = s.id " +
                    "INNER JOIN feedback f ON f.product_id = p.id " +
                    "WHERE s.id = ?1 " +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = DATE(f.time) " +
                    "    AND EXTRACT(HOUR FROM f.time) >= ?2 " +
                    "    AND EXTRACT(HOUR FROM f.time) <= ?3",
            nativeQuery = true)
    Integer getFeedbackAnalystByHourRangePreviousDay(Integer id, int prev, int i);

    @Query(
            value = "SELECT COUNT(f.id) " +
                    "FROM shop s " +
                    "INNER JOIN product p ON p.shop_id = s.id " +
                    "INNER JOIN feedback f ON f.product_id = p.id " +
                    "WHERE s.id = ?1 " +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM f.time) " +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM f.time) " +
                    "  AND EXTRACT(DAY FROM f.time) >= ?2 " +
                    "  AND EXTRACT(DAY FROM f.time) <= ?3",
            nativeQuery = true)
    Integer getFeedbackAnalystByDayRange(Integer id, int prev, int i);

    @Query(
            value = "SELECT COUNT(f.id) " +
                    "FROM shop s " +
                    "INNER JOIN product p ON p.shop_id = s.id " +
                    "INNER JOIN feedback f ON f.product_id = p.id " +
                    "WHERE s.id = ?1 " +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM f.time) " +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(MONTH FROM f.time) " +
                    "  AND EXTRACT(DAY FROM f.time) >= ?2 " +
                    "  AND EXTRACT(DAY FROM f.time) <= ?3",
            nativeQuery = true)
    Integer getFeedbackAnalystByDayRangePreviousMonth(Integer id, int prev, int i);

    @Query(
            value = "SELECT COUNT(f.id) " +
                    "FROM shop s " +
                    "INNER JOIN product p ON p.shop_id = s.id " +
                    "INNER JOIN feedback f ON f.product_id = p.id " +
                    "WHERE s.id = ?1 " +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM f.time) " +
                    "   AND EXTRACT(MONTH FROM f.time) >= ?2 " +
                    "   AND EXTRACT(MONTH FROM f.time) <= ?3",
            nativeQuery = true)
    Integer getFeedbackAnalystByMonthRange(Integer id, int prev, int i);

    @Query(
            value = "SELECT COUNT(f.id) " +
                    "FROM shop s " +
                    "INNER JOIN product p ON p.shop_id = s.id " +
                    "INNER JOIN feedback f ON f.product_id = p.id " +
                    "WHERE s.id = ?1 " +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(YEAR FROM f.time) " +
                    "   AND EXTRACT(MONTH FROM f.time) >= ?2 " +
                    "   AND EXTRACT(MONTH FROM f.time) <= ?3",
            nativeQuery = true)
    Integer getFeedbackAnalystByMonthRangePreviousYear(Integer id, int prev, int i);
}
