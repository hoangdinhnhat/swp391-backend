package com.swp391.backend.model.report;

import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public Report getByReporterAndShop(User reporter, Shop shop)
    {
        return reportRepository.findByReporterAndShop(reporter, shop);
    }

    public Report getByReporterAndProduct(User reporter, Product product)
    {
        return reportRepository.findByReporterAndProduct(reporter, product);
    }

    public Report getByReporterAndOrder(User reporter, Order order)
    {
        return reportRepository.findByReporterAndOrder(reporter, order);
    }

    public void deleteById(Integer id) {
        reportRepository.deleteById(id);
    }

    public Report getById(Integer id)
    {
        return reportRepository.findById(id).orElse(null);
    }

    public List<Report> getProductReport() {
        return reportRepository.findByProductIsNotNullAndAction("UNPROCESS");
    }

    public List<Report> getShopReport() {
        return reportRepository.findByShopIsNotNullAndAction("UNPROCESS");
    }

    public List<Report> getOrderReport() {
        return reportRepository.findByOrderIsNotNullAndAction("UNPROCESS");
    }
}
