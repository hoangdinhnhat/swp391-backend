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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Integer getNumberOfOrderInCurrentDay(Shop shop)
    {
        return orderRepository.getNumberOfOrderInDate(shop.getId());
    }

    public List<Integer> getNumberOfOrderAnalystInDay(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        for (int i = 0; i <= hourRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByHourRange(shop.getId(), i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInWeek(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;

        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInMonth(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));

        for (int i = 1; i <= weekRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), i * 7 );
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInYear(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();

        for (int i = 1; i <= monthRange; i += 1)
        {
            Integer num = orderRepository.getNumberOfOrderAnalystByMonthRange(shop.getId(), i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInDay(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();
        int hourRange = LocalDateTime.now().getHour();
        for (int i = 0; i <= hourRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByHourRange(shop.getId(), i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInWeek(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;

        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), i);
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInMonth(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));

        for (int i = 1; i <= weekRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), i * 7 );
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInYear(Shop shop)
    {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();

        for (int i = 1; i <= monthRange; i += 1)
        {
            Integer num = orderRepository.getRevenueAnalystByMonthRange(shop.getId(), i);
            rs.add(num);
        }

        return rs;
    }


    public void init()
    {
        User user = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");
        Shop shop = shopService.getShopById(1);
        Product p1 = productService.getProductById(21);
        Product p2 = productService.getProductById(22);

        Order order = Order.builder()
                .id("2023061601")
                .user(user)
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
