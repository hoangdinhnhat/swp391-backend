package com.swp391.backend.model.report;

import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByProductIsNotNullAndAction(String action);

    List<Report> findByShopIsNotNullAndAction(String action);
    List<Report> findByOrderIsNotNullAndAction(String action);

    Report findByReporterAndShop(User reporter, Shop shop);

    Report findByReporterAndProduct(User reporter, Product product);

    Report findByReporterAndOrder(User reporter, Order order);

}
