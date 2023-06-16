package com.swp391.backend.model.subscription;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final ShopService shopService;

    public Subscription save(Subscription subscription)
    {
        return subscriptionRepository.save(subscription);
    }

    public void init()
    {
        User user = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");
        User user2 = (User) userService.loadUserByUsername("vuducthien@gmail.com");
        Shop shop = shopService.getShopById(1);

        SubscriptionId id = new SubscriptionId();
        id.setShopId(shop.getId());
        id.setUserId(user.getId());

        Subscription subscription = Subscription.builder()
                .id(id)
                .shop(shop)
                .subscriptionTime(new Date())
                .user(user)
                .build();
        save(subscription);

        SubscriptionId id2 = new SubscriptionId();
        id2.setShopId(shop.getId());
        id2.setUserId(user2.getId());

        Subscription subscription2 = Subscription.builder()
                .id(id2)
                .shop(shop)
                .subscriptionTime(new Date())
                .user(user2)
                .build();
        save(subscription2);
    }
}
