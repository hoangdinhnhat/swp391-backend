package com.swp391.backend.model.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Integer getNumberOfOrderInCurrentDay()
    {
        Date date = Date.valueOf(LocalDate.now());
        return orderRepository.getNumberOfOrderInDate(date);
    }
}
