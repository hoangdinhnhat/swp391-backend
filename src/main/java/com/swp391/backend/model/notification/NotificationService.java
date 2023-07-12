package com.swp391.backend.model.notification;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Sort sort = Sort.by("createdAt").descending();
        return notificationRepository.findByUser(user, sort);
    }
    public List<Notification> getNotificationByUser(User user, Integer page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return notificationRepository.findByUser(user, pageable);
    }

    public List<Notification> getNotificationByShop(Shop shop) {
        Sort sort = Sort.by("createdAt").descending();
        return notificationRepository.findByShop(shop, sort);
    }

    public List<Notification> getNotificationByShop(Shop shop, Integer page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return notificationRepository.findByShop(shop, pageable);
    }

    public int ceil(float num)
    {
        int rounded = Math.round(num);
        if (rounded < num)
        {
            rounded += 1;
        }
        return rounded;
    }

    public Integer getMaxPage(Shop shop)
    {
        Sort sort = Sort.by("createdAt").descending();
        var notifies =  notificationRepository.findByShop(shop, sort);
        float div = notifies.size() * 1.0f / 5;
        return ceil(div);
    }

    public Integer getMaxPage(User user)
    {
        Sort sort = Sort.by("createdAt").descending();
        var notifies =  notificationRepository.findByUser(user, sort);
        float div = notifies.size() * 1.0f / 5;
        return ceil(div);
    }
}
