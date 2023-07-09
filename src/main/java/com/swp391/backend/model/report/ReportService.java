package com.swp391.backend.model.report;

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
}
