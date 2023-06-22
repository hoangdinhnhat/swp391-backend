package com.swp391.backend.model.order;

import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.orderDetails.OrderDetailsId;
import com.swp391.backend.model.orderDetails.OrderDetailsService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ShopService shopService;
    private final OrderDetailsService orderDetailsService;
    private final ProductService productService;

    public Order save(Order order)
    {
        return orderRepository.save(order);
    }

    public Order getById(String id)
    {
        return orderRepository.findById(id).orElse(null);
    }

    public Integer getNumberOfOrderInCurrentDay(Shop shop)
    {
        return orderRepository.getNumberOfOrderInDate(shop.getId());
    }

    public List<Integer> getNumberOfOrderAnalystInDay(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        int prev = 0;
        for (int i = 0; i <= hourRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByHourRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInPreviousDay(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        int prev = 0;
        for (int i = 0; i <= hourRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByHourRangePreviousDay(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInWeek(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        int prev = 7 * weekRange + 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInMonth(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), prev, i * 7 );
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInYear(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        int prev = 1;
        for (int i = 1; i <= monthRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByMonthRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInAll(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int endYearRange = LocalDateTime.now().getYear();
        int beginYearRange = shop.getJoinTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .getYear();

        int prev = beginYearRange;
        for (int i = beginYearRange; i <= endYearRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByYearRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInDay(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        int prev = 0;
        for (int i = 0; i <= hourRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByHourRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInPreviousDay(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        int prev = 0;
        for (int i = 0; i <= hourRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByHourRangePreviousDay(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInWeek(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        int prev = 7 * weekRange + 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInMonth(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), prev, i * 7 );
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInYear(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        int prev = 1;
        for (int i = 1; i <= monthRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByMonthRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInAll(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int endYearRange = LocalDateTime.now().getYear();
        int beginYearRange = shop.getJoinTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .getYear();

        int prev = beginYearRange;
        for (int i = beginYearRange; i <= endYearRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByYearRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Order> getByShopAndId(Shop shop, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());
        return orderRepository.findByShopAndIdContainingIgnoreCase(shop, kw, pageable);
    }

    public List<Order> getByShopAndStatusAndId(Shop shop, OrderStatus status, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());;
        return orderRepository.findByShopAndStatusAndIdContainingIgnoreCase(shop, status, kw, pageable);
    }

    public void init()
    {
        User user = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");
        Shop shop = shopService.getShopById(2);
        Product p1 = productService.getProductById(22);
        Product p2 = productService.getProductById(25);

        Order order = Order.builder()
                .id("2023061601")
                .user(user)
                .status(OrderStatus.COMPLETED)
                .payment("COD")
                .description(p1.getDescription().substring(0, 150))
                .shop(shop)
                .createdTime(new Date())
                .build();
        save(order);

        OrderDetailsId id1 = new OrderDetailsId();
        id1.setOrderId(order.getId());
        id1.setProductId(p1.getId());

        OrderDetails orderDetails1 = OrderDetails.builder()
                .id(id1)
                .order(order)
                .quantity(2)
                .product(p1)
                .build();

        OrderDetailsId id2 = new OrderDetailsId();
        id1.setOrderId(order.getId());
        id1.setProductId(p2.getId());

        OrderDetails orderDetails2 = OrderDetails.builder()
                .id(id2)
                .order(order)
                .quantity(1)
                .product(p2)
                .build();

        orderDetailsService.save(orderDetails1);
        orderDetailsService.save(orderDetails2);

        Order order2 = Order.builder()
                .id("2023061602")
                .user(user)
                .status(OrderStatus.COMPLETED)
                .payment("COD")
                .description(p1.getDescription().substring(0, 150))
                .shop(shop)
                .createdTime(new Date())
                .build();
        save(order2);

        OrderDetailsId id12 = new OrderDetailsId();
        id12.setOrderId(order2.getId());
        id12.setProductId(p1.getId());

        OrderDetails orderDetails12 = OrderDetails.builder()
                .id(id12)
                .order(order2)
                .quantity(2)
                .product(p1)
                .build();

        OrderDetailsId id22 = new OrderDetailsId();
        id22.setOrderId(order2.getId());
        id22.setProductId(p2.getId());

        OrderDetails orderDetails22 = OrderDetails.builder()
                .id(id22)
                .order(order2)
                .quantity(1)
                .product(p2)
                .build();

        orderDetailsService.save(orderDetails12);
        orderDetailsService.save(orderDetails22);
    }
}