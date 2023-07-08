package com.swp391.backend.model.subscription;

import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final ShopService shopService;

    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }

    public List<Integer> getNumberOfSubscriptionAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByHourRange(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInPreviousDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByHourRangePreviousDay(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByDayRange(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInPreviousWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 2;
        for (int i = 7 * weekRange + 1; i <= 7 * (weekRange + 1); i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByDayRange(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByDayRange(shop.getId(), prev, i * 7);
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInPreviousMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByDayRangePreviousMonth(shop.getId(), prev, i * 7);
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByMonthRange(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInPreviousYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByMonthRangePreviousYear(shop.getId(), i, i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSubscriptionAnalystInAll(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int endYearRange = LocalDateTime.now().getYear();
        int beginYearRange = shop.getJoinTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .getYear();

        int prev = beginYearRange;
        for (int i = beginYearRange; i <= endYearRange; i += 1) {
            Integer num = subscriptionRepository.getSubscriptionAnalystByYearRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public void init() {
        User user = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");
        User user2 = (User) userService.loadUserByUsername("nhathdse160377@fpt.edu.vn");
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
