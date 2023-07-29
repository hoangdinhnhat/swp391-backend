package com.swp391.backend.model.orderDetails;

import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, OrderDetailsId> {
    OrderDetails findByOrderAndProduct(Order order, Product product);

    @Query(
            value = "SELECT SUM(od.quantity) " +
                    "FROM shop s " +
                    "INNER JOIN _order o ON o.shop_id = s.id " +
                    "INNER JOIN order_details od ON od.order_id = o.id " +
                    "WHERE " +
                    "    s.id = ?1 " +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time) " +
                    "    AND EXTRACT(HOUR FROM o.created_time) >= ?2 " +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfSoldProductAnalystByHourRange(Integer id, int prev, int i);

    @Query(
            value = "SELECT SUM(od.quantity) " +
                    "FROM shop s " +
                    "INNER JOIN _order o ON o.shop_id = s.id " +
                    "INNER JOIN order_details od ON od.order_id = o.id " +
                    "WHERE " +
                    "    s.id = ?1 " +
                    "    AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = DATE(o.created_time) " +
                    "    AND EXTRACT(HOUR FROM o.created_time) >= ?2 " +
                    "    AND EXTRACT(HOUR FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfSoldProductAnalystByHourRangePreviousDay(Integer id, int prev, int i);

    @Query(
            value = "SELECT SUM(od.quantity) " +
                    "FROM shop s " +
                    "INNER JOIN _order o ON o.shop_id = s.id " +
                    "INNER JOIN order_details od ON od.order_id = o.id " +
                    "WHERE " +
                    "    s.id = ?1 " +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time) " +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time) " +
                    "  AND EXTRACT(DAY FROM o.created_time) >= ?2 " +
                    "  AND EXTRACT(DAY FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfSoldProductAnalystByDayRange(Integer id, int prev, int i);

    @Query(
            value = "SELECT SUM(od.quantity) " +
                    "FROM shop s " +
                    "INNER JOIN _order o ON o.shop_id = s.id " +
                    "INNER JOIN order_details od ON od.order_id = o.id " +
                    "WHERE " +
                    "    s.id = ?1 " +
                    "  AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time) " +
                    "  AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(MONTH FROM o.created_time) " +
                    "  AND EXTRACT(DAY FROM o.created_time) >= ?2 " +
                    "  AND EXTRACT(DAY FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfSoldProductAnalystByDayRangePreviousMonth(Integer id, int prev, int i);

    @Query(
            value = "SELECT SUM(od.quantity) " +
                    "FROM shop s " +
                    "INNER JOIN _order o ON o.shop_id = s.id " +
                    "INNER JOIN order_details od ON od.order_id = o.id " +
                    "WHERE " +
                    "    s.id = ?1 " +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time) " +
                    "   AND EXTRACT(MONTH FROM o.created_time) >= ?2 " +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfSoldProductAnalystByMonthRange(Integer id, int prev, int i);

    @Query(
            value = "SELECT SUM(od.quantity) " +
                    "FROM shop s " +
                    "INNER JOIN _order o ON o.shop_id = s.id " +
                    "INNER JOIN order_details od ON od.order_id = o.id " +
                    "WHERE " +
                    "    s.id = ?1 " +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - 1 = EXTRACT(YEAR FROM o.created_time) " +
                    "   AND EXTRACT(MONTH FROM o.created_time) >= ?2 " +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 " +
                    "AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true)
    Integer getNumberOfSoldProductAnalystByMonthRangePreviousYear(Integer id, int prev, int i);

    List<OrderDetails> findByOrder(Order order);
}
