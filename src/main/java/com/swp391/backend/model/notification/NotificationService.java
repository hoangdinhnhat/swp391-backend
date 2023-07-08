package com.swp391.backend.model.notification;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification getById(Integer id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> saveAllAndFlush(List<Notification> notifications) {
        return notificationRepository.saveAllAndFlush(notifications);
    }

    public Integer getUnreadNotificationByUser(User user) {
        return notificationRepository.findByUserAndRead(user, false).size();
    }

    public List<Notification> getNotificationByUser(User user) {
        return notificationRepository.findByUser(user);
    }

    public List<Notification> getNotificationByShop(Shop shop) {
        return notificationRepository.findByShop(shop);
    }
}
