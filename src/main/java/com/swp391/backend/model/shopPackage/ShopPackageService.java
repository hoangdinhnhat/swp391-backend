package com.swp391.backend.model.shopPackage;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shopPlan.ShopPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopPackageService {

    private final ShopPackageRepository shopPackageRepository;


    public ShopPackage save(ShopPackage shopPackage) {
        ShopPlan shopPlan = shopPackage.getShopPlan();
        Integer duration = shopPlan.getDuration();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(duration);
        Date date = Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());

        if (!shopPlan.getPlan().equals("UNREGISTE")) {
            shopPackage.setStartTime(new Date());
            shopPackage.setEndTime(date);
        }
        return shopPackageRepository.save(shopPackage);
    }

    public boolean checkShopPackage(Shop shop) {
        int size = shop.getShopPackages().size();
        var shopPackage = shop.getShopPackages().get(size - 1);
        Date endTime = shopPackage.getEndTime();
        return !(endTime == null || endTime.before(new Date()));
    }

    public List<Double> getWeeklyRevenue() {
        List<Double> rs = new ArrayList<>();
        var dow = LocalDateTime.now().getDayOfWeek();
        int day = dow.getValue();
        int distance = day - 1;
        for (int i = 0; i <= distance; i++) {
            Double reven = shopPackageRepository.getWeeklyRevenueAnalyst(i);
            if (reven == null) reven = 0.0;
            rs.add(reven);
        }
        return rs;
    }

    public void init() {

    }
}
