package com.swp391.backend.model.notification;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserAndRead(User user, boolean read);

    List<Notification> findByUser(User user, Sort sort);
    List<Notification> findByUser(User user, Pageable pageable);

    List<Notification> findByShop(Shop shop, Sort sort);

    List<Notification> findByShop(Shop shop, Pageable pageable);
}
