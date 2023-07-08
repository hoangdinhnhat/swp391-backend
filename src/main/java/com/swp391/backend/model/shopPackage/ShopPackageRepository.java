package com.swp391.backend.model.shopPackage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopPackageRepository extends JpaRepository<ShopPackage, Integer> {
    @Query(
            value = "SELECT SUM(spl.price) " +
                    "FROM shop_package sp INNER JOIN shop_plan spl ON sp.shop_plan_id = spl.plan " +
                    "WHERE DATE(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') - ?1 = DATE(sp.start_time) " +
                    "AND sp.shop_plan_id <> 'UNREGISTE'",
            nativeQuery = true
    )
    Double getWeeklyRevenueAnalyst(int i);
}
