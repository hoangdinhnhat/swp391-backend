/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.swp391.backend.model.categoryGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Lenovo
 */
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Integer> {

    @Query(
            value = "SELECT cg.id, cg.name, cg.category_id\n" +
                    "FROM category_group cg\n" +
                    "INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "INNER JOIN order_details od on p.id = od.product_id\n" +
                    "INNER JOIN _order o on od.order_id = o.id\n" +
                    "INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "AND EXTRACT(HOUR FROM o.created_time) <= ?2\n" +
                    "GROUP BY cg.id, cg.name, cg.category_id\n" +
                    "ORDER BY SUM(od.quantity) DESC",
            nativeQuery = true
    )
    List<CategoryGroup> getTopThreeSoldCategoryGroupByHourRange(Integer shopId, Integer hourRange);

    @Query(
            value = "SELECT cg.id, cg.name, cg.category_id\n" +
                    "FROM category_group cg\n" +
                    "INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "INNER JOIN order_details od on p.id = od.product_id\n" +
                    "INNER JOIN _order o on od.order_id = o.id\n" +
                    "INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "   AND EXTRACT(DAY FROM o.created_time) <= ?2\n" +
                    "GROUP BY cg.id, cg.name, cg.category_id\n" +
                    "ORDER BY SUM(od.quantity) DESC",
            nativeQuery = true
    )
    List<CategoryGroup> getTopThreeSoldCategoryGroupByDayRange(Integer shopId, Integer dayRange);

    @Query(
            value = "SELECT cg.id, cg.name, cg.category_id\n" +
                    "FROM category_group cg\n" +
                    "INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "INNER JOIN order_details od on p.id = od.product_id\n" +
                    "INNER JOIN _order o on od.order_id = o.id\n" +
                    "INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?2\n" +
                    "GROUP BY cg.id, cg.name, cg.category_id\n" +
                    "ORDER BY SUM(od.quantity) DESC",
            nativeQuery = true
    )
    List<CategoryGroup> getTopThreeSoldCategoryGroupByMonthRange(Integer shopId, Integer monthRange);

    @Query(
            value = "SELECT cg.id, cg.name, cg.category_id\n" +
                    "FROM category_group cg\n" +
                    "INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "INNER JOIN order_details od on p.id = od.product_id\n" +
                    "INNER JOIN _order o on od.order_id = o.id\n" +
                    "INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "   AND EXTRACT(YEAR FROM o.created_time) <= ?2\n" +
                    "GROUP BY cg.id, cg.name, cg.category_id\n" +
                    "ORDER BY SUM(od.quantity) DESC",
            nativeQuery = true
    )
    List<CategoryGroup> getTopThreeSoldCategoryGroupByYearRange(Integer shopId, Integer hourRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category_group cg\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on p.id = od.product_id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND cg.id = ?2\n" +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "  AND EXTRACT(HOUR FROM o.created_time) <= ?3",
            nativeQuery = true
    )
    Integer getSoldByCategoryGroupByHourRange(Integer shopId, Integer categoryGroupId, Integer hourRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category_group cg\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on p.id = od.product_id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND cg.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "   AND EXTRACT(DAY FROM o.created_time) <= ?3",
            nativeQuery = true
    )
    Integer getSoldByCategoryGroupByDayRange(Integer shopId, Integer categoryGroupId, Integer dayRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category_group cg\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on p.id = od.product_id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND cg.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3",
            nativeQuery = true
    )
    Integer getSoldByCategoryGroupByMonthRange(Integer shopId, Integer categoryGroupId, Integer monthRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category_group cg\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on p.id = od.product_id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND cg.id = ?2\n" +
                    "  AND EXTRACT(YEAR FROM o.created_time) <= ?3",
            nativeQuery = true
    )
    Integer getSoldByCategoryGroupByYearRange(Integer shopId, Integer categoryGroupId, Integer yearRange);
}
