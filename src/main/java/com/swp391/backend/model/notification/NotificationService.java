package com.swp391.backend.model.notification;

import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification)
    {
        return notificationRepository.save(notification);
    }

    public List<Notification> saveAllAndFlush(List<Notification> notifications)
    {
        return notificationRepository.saveAllAndFlush(notifications);
    }

    public Integer getUnreadNotificationByUser(User user)
    {
        return notificationRepository.findByUserAndRead(user, false).size();
    }

    public List<Notification> getNotificationByUser(User user)
    {
        return notificationRepository.findByUser(user);
    }
}
