package com.swp391.backend.model.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByProductIsNotNullAndAction(String action);

    List<Report> findByShopIsNotNullAndAction(String action);
}
