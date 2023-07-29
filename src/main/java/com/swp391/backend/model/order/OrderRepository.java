package com.swp391.backend.model.order;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, String>, JpaRepository<Order, String> {

    //Current Time Chart
    @Query(
            value = "SELECT COUNT(o.id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n",
            nativeQuery = true)
    Integer getNumberOfOrderInDate();

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) >= ?2\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByHourRange(Integer shopId, Integer beginHourRange, Integer endHourRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = DATE(o.created_time)\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) >= ?2\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByHourRangePreviousDay(Integer shopId, Integer beginHourRange, Integer endHourRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE o.shop_id = ?1\n" +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "  AND EXTRACT(DAY FROM o.created_time) >= ?2\n" +
                    "  AND EXTRACT(DAY FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByDayRange(Integer shopId, Integer beginDayRange, Integer endDayRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE o.shop_id = ?1\n" +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(MONTH FROM o.created_time)\n" +
                    "  AND EXTRACT(DAY FROM o.created_time) >= ?2\n" +
                    "  AND EXTRACT(DAY FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByDayRangePreviousMonth(Integer shopId, Integer beginDayRange, Integer endDayRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) >= ?2\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByMonthRange(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) >= ?2\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByMonthRangePreviousYEar(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM o.created_time) >= ?2\n" +
                    "   AND EXTRACT(YEAR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByYearRange(Integer shopId, Integer beginYearRange, Integer endYearRange);

    @Query(
            value = "SELECT SUM(o.sell_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) >= ?2\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getRevenueAnalystByHourRange(Integer shopId, Integer beginHourRange, Integer endHourRange);

    @Query(
            value = "SELECT SUM(o.sell_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = DATE(o.created_time)\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) >= ?2\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getRevenueAnalystByHourRangePreviousDay(Integer shopId, Integer beginHourRange, Integer endHourRange);

    @Query(
            value = "SELECT SUM(o.sell_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "    AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "    AND EXTRACT(DAY FROM o.created_time) >= ?2\n" +
                    "    AND EXTRACT(DAY FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getRevenueAnalystByDayRange(Integer shopId, Integer beginDayRange, Integer endDayRange);

    @Query(
            value = "SELECT SUM(o.sell_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "    AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(MONTH FROM o.created_time)\n" +
                    "    AND EXTRACT(DAY FROM o.created_time) >= ?2\n" +
                    "    AND EXTRACT(DAY FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getRevenueAnalystByDayRangePreviousMonth(Integer shopId, Integer beginDayRange, Integer endDayRange);

    @Query(
            value = "SELECT SUM(o.sell_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) >= ?2\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getRevenueAnalystByMonthRange(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    @Query(
            value = "SELECT SUM(o.sell_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) >= ?2\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getRevenueAnalystByMonthRangePreviousYear(Integer shopId, Integer beginMonthRange, Integer endMonthRange);

    @Query(
            value = "SELECT SUM(o.sellPrice)\n" +
                    "FROM Order o\n" +
                    "WHERE\n" +
                    "   o.shop = ?1\n" +
                    "   AND EXTRACT(YEAR FROM o.createdTime) >= ?2\n" +
                    "   AND EXTRACT(YEAR FROM o.createdTime) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')")
    Integer getRevenueAnalystByYearRange(Shop shop, Integer beginYearRange, Integer endYearRange);

    List<Order> findByShopAndIdContainingIgnoreCase(Shop shop, String id, Pageable pageable);

    List<Order> findByShopAndIdContainingIgnoreCase(Shop shop, String id);

    List<Order> findByShopAndStatusAndIdContainingIgnoreCase(Shop shop, OrderStatus status, String id, Pageable pageable);

    List<Order> findByShopAndStatusAndIdContainingIgnoreCase(Shop shop, OrderStatus status, String id);

    List<Order> findByUserAndIdContainingIgnoreCase(User user, String id, Pageable pageable);

    List<Order> findByUserAndIdContainingIgnoreCase(User user, String id);

    List<Order> findByUserAndStatusAndIdContainingIgnoreCase(User user, OrderStatus status, String id, Pageable pageable);

    List<Order> findByStatusAndAndSpecialAndIdContainingIgnoreCase(OrderStatus status, Boolean special, String id, Pageable pageable);

    List<Order> findByStatusAndAndSpecialAndIdContainingIgnoreCase(OrderStatus status, Boolean special, String id);

    List<Order> findByUserAndStatusAndIdContainingIgnoreCase(User user, OrderStatus status, String id);
}
