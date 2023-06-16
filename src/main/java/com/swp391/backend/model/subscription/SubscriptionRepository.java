package com.swp391.backend.model.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE s.shop_id = ?1\n" +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(s.subscription_time)\n" +
                    "  AND EXTRACT(HOUR FROM s.subscription_time) <= ?2",
            nativeQuery = true)
    Integer getSubscriptionAnalystByHourRange(Integer shopId, Integer hourRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(s.subscription_time)\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM s.subscription_time)\n" +
                    "   AND EXTRACT(DAY FROM s.subscription_time) <= ?2",
            nativeQuery = true)
    Integer getSubscriptionAnalystByDayRange(Integer shopId, Integer dayRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "   AND EXTRACT(MONTH FROM s.subscription_time) <= ?2",
            nativeQuery = true)
    Integer getSubscriptionAnalystByMonthRange(Integer shopId, Integer monthRange);
}
