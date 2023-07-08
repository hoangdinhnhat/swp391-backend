package com.swp391.backend.model.shopPlan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopPlanRepository extends JpaRepository<ShopPlan, Integer> {
    ShopPlan findByPlan(String plan);
}
