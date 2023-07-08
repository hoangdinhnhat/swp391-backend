package com.swp391.backend.model.subscription;

import com.swp391.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionId> {
    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE s.shop_id = ?1\n" +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(s.subscription_time)\n" +
                    "  AND EXTRACT(HOUR FROM s.subscription_time) >= ?2\n" +
                    "  AND EXTRACT(HOUR FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByHourRange(Integer shopId, Integer beginHourRange, Integer endHourRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE s.shop_id = ?1\n" +
                    "    AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "    AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM s.subscription_time)\n" +
                    "    AND EXTRACT(DAY FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(DAY FROM s.subscription_time)\n" +
                    "    AND EXTRACT(HOUR FROM s.subscription_time) >= ?2\n" +
                    "    AND EXTRACT(HOUR FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByHourRangePreviousDay(Integer shopId, Integer beginHourRange, Integer endHourRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM s.subscription_time)\n" +
                    "   AND EXTRACT(DAY FROM s.subscription_time) >= ?2\n" +
                    "   AND EXTRACT(DAY FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByDayRange(Integer shopId, Integer beginDayRange, Integer endDayRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(MONTH FROM s.subscription_time)\n" +
                    "   AND EXTRACT(DAY FROM s.subscription_time) >= ?2\n" +
                    "   AND EXTRACT(DAY FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByDayRangePreviousMonth(Integer shopId, Integer beginDayRange, Integer endDayRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "   AND EXTRACT(MONTH FROM s.subscription_time) >= ?2\n" +
                    "   AND EXTRACT(MONTH FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByMonthRange(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(YEAR FROM s.subscription_time)\n" +
                    "   AND EXTRACT(MONTH FROM s.subscription_time) >= ?2\n" +
                    "   AND EXTRACT(MONTH FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByMonthRangePreviousYear(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    @Query(
            value = "SELECT COUNT(s.shop_id)\n" +
                    "FROM subscription s\n" +
                    "WHERE\n" +
                    "   s.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM s.subscription_time) >= ?2\n" +
                    "   AND EXTRACT(YEAR FROM s.subscription_time) <= ?3",
            nativeQuery = true)
    Integer getSubscriptionAnalystByYearRange(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    List<Subscription> findByUser(User user);
}
