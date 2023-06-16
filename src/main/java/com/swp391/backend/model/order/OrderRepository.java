package com.swp391.backend.model.order;

import com.swp391.backend.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, String>, JpaRepository<Order, String> {
    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n",
            nativeQuery = true)
    Integer getNumberOfOrderInDate(Integer shopId);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?2",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByHourRange(Integer shopId, Integer hourRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE o.shop_id = ?1\n" +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "  AND EXTRACT(DAY FROM o.created_time) <= ?2",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByDayRange(Integer shopId, Integer dayRange);

    @Query(
            value = "SELECT COUNT(o.shop_id)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?2",
            nativeQuery = true)
    Integer getNumberOfOrderAnalystByMonthRange(Integer shopId, Integer monthRange);

    @Query(
            value = "SELECT SUM(o.total_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?2",
            nativeQuery = true)
    Integer getRevenueAnalystByHourRange(Integer shopId, Integer hourRange);

    @Query(
            value = "SELECT SUM(o.total_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "    o.shop_id = ?1\n" +
                    "    AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "    AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "    AND EXTRACT(DAY FROM o.created_time) <= ?2",
            nativeQuery = true)
    Integer getRevenueAnalystByDayRange(Integer shopId, Integer dayRange);

    @Query(
            value = "SELECT SUM(o.total_price)\n" +
                    "FROM _order o\n" +
                    "WHERE\n" +
                    "   o.shop_id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?2",
            nativeQuery = true)
    Integer getRevenueAnalystByMonthRange(Integer shopId, Integer monthRange);
}
