package com.swp391.backend.model.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(
            value = "SELECT distinct c.id\n" +
                    "FROM shop s\n" +
                    "         INNER JOIN product p on s.id = p.shop_id\n" +
                    "         INNER JOIN category_group cg on p.category_group_id = cg.id\n" +
                    "         INNER JOIN category c on cg.category_id = c.id\n" +
                    "WHERE s.id = ?1",
            nativeQuery = true
    )
    List<Integer> getCategoryInShop(Integer shopId);

    @Query(
            value = "SELECT distinct c.id\n" +
                    "FROM category c ORDER BY c.id ASC ",
            nativeQuery = true
    )
    List<Integer> getCategory();

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "  AND EXTRACT(HOUR FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getCategoryAnalystByHourRange(Integer shopId, Integer categoryId, Integer hourRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "   AND EXTRACT(DAY FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getCategoryAnalystByDayRange(Integer shopId, Integer categoryId, Integer dayRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getCategoryAnalystByMonthRange(Integer shopId, Integer categoryId, Integer monthRange);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "WHERE s.id = ?1\n" +
                    "   AND c.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getCategoryAnalystByYearRange(Integer shopId, Integer categoryId, Integer yearRange);

    @Query(
            value = "SELECT SUM(p.price * od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "         INNER JOIN product_sale ps on p.id = ps.product_id\n" +
                    "        INNER JOIN sale_event se on ps.sale_event_id = se.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "  AND DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = DATE(o.created_time)\n" +
                    "  AND EXTRACT(HOUR FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getRevenueByCategoryAndHourRange(Integer shopId, Integer categoryId, Integer hourRange);

    @Query(
            value = "SELECT SUM(p.price * od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "         INNER JOIN product_sale ps on p.id = ps.product_id\n" +
                    "        INNER JOIN sale_event se on ps.sale_event_id = se.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(MONTH FROM o.created_time)\n" +
                    "   AND EXTRACT(DAY FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getRevenueByCategoryAndDayRange(Integer shopId, Integer categoryId, Integer dayRange);

    @Query(
            value = "SELECT SUM(p.price * od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "         INNER JOIN product_sale ps on p.id = ps.product_id\n" +
                    "        INNER JOIN sale_event se on ps.sale_event_id = se.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') = EXTRACT(YEAR FROM o.created_time)\n" +
                    "   AND EXTRACT(MONTH FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getRevenueByCategoryAndMonthRange(Integer shopId, Integer categoryId, Integer monthRange);

    @Query(
            value = "SELECT SUM(p.price * od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "         INNER JOIN product_sale ps on p.id = ps.product_id\n" +
                    "        INNER JOIN sale_event se on ps.sale_event_id = se.id\n" +
                    "WHERE s.id = ?1\n" +
                    "  AND c.id = ?2\n" +
                    "   AND EXTRACT(YEAR FROM o.created_time) <= ?3 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getRevenueByCategoryAndYearRange(Integer shopId, Integer categoryId, Integer yearRange);

    @Query(
            value = "SELECT SUM(p.price * od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "         INNER JOIN product_sale ps on p.id = ps.product_id\n" +
                    "        INNER JOIN sale_event se on ps.sale_event_id = se.id\n" +
                    "WHERE s.id = ?1 AND c.id = ?2 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getRevenueByCategory(Integer shopId, Integer categoryId);

    @Query(
            value = "SELECT SUM(od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "         INNER JOIN shop s on o.shop_id = s.id\n" +
                    "         INNER JOIN product_sale ps on p.id = ps.product_id\n" +
                    "        INNER JOIN sale_event se on ps.sale_event_id = se.id\n" +
                    "WHERE s.id = ?1 AND c.id = ?2 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getNumberOfCategory(Integer shopId, Integer categoryId);

    @Query(
            value = "SELECT COUNT(p.id)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "GROUP BY c.id " +
                    "HAVING c.id = ?1",
            nativeQuery = true
    )
    Integer getNumberOfCategory(Integer categoryId);

    @Query(
            value = "SELECT SUM(p.price * od.quantity)\n" +
                    "FROM category c\n" +
                    "         INNER JOIN category_group cg on c.id = cg.category_id\n" +
                    "         INNER JOIN product p on cg.id = p.category_group_id\n" +
                    "         INNER JOIN order_details od on od.product_id = p.id\n" +
                    "         INNER JOIN _order o on od.order_id = o.id\n" +
                    "WHERE DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - ?1 = DATE(o.created_time)" +
                    "AND c.id = ?2 AND (o.status = 'SHIPPING' OR o.status = 'COMPLETED')",
            nativeQuery = true
    )
    Integer getRevenueByCategoryLast14Day(Integer day, Integer categoryId);
}
