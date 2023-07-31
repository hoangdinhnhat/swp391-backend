package com.swp391.backend.model.shopPlan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopPlanService {
    private final ShopPlanRepository shopPlanRepository;

    public ShopPlan save(ShopPlan shopPlan) {
        return shopPlanRepository.save(shopPlan);
    }

    public ShopPlan getByPlan(String plan) {
        return shopPlanRepository.findByPlan(plan);
    }

    public void init() {
        var shopPlan = ShopPlan.builder()
                .plan("UNREGISTE")
                .duration(0)
                .build();

        var shopPlan1 = ShopPlan.builder()
                .plan("FREE TRIAL")
                .duration(7)
                .build();

        var shopPlan2 = ShopPlan.builder()
                .plan("GOLD")
                .duration(30 * 3)
                .price(0.05f)
                .build();

        var shopPlan3 = ShopPlan.builder()
                .plan("PLATINUM")
                .duration(30 * 6)
                .price(115)
                .build();

        var shopPlan4 = ShopPlan.builder()
                .plan("DIAMOND")
                .duration(30 * 9)
                .price(165)
                .build();

        var shopPlan5 = ShopPlan.builder()
                .plan("TITANIUM")
                .duration(30 * 12)
                .price(220)
                .build();

        save(shopPlan);
        save(shopPlan1);
        save(shopPlan2);
        save(shopPlan3);
        save(shopPlan4);
        save(shopPlan5);
    }
}
