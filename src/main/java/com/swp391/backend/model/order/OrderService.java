package com.swp391.backend.model.order;

import com.swp391.backend.model.district.District;
import com.swp391.backend.model.district.DistrictService;
import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.orderDetails.OrderDetailsId;
import com.swp391.backend.model.orderDetails.OrderDetailsService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.province.Province;
import com.swp391.backend.model.province.ProvinceService;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.receiveinfo.ReceiveInfoService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserService;
import com.swp391.backend.model.ward.Ward;
import com.swp391.backend.model.ward.WardService;
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
    private final WardService wardService;
    private final DistrictService districtService;
    private final ProvinceService provinceService;
    private final ReceiveInfoService receiveInfoService;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public Order getById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Integer getTotalOrder() {
        return orderRepository.findAll().size();
    }

    public Integer getNumberOfOrderInCurrentDay() {
        return orderRepository.getNumberOfOrderInDate();
    }

    public List<Integer> getNumberOfOrderAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByHourRange(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInPreviousDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByHourRangePreviousDay(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInPreviousWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 2;
        for (int i = 7 * weekRange + 1; i <= 7 * (weekRange + 1); i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRange(shop.getId(), prev, i * 7);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInPreviousMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByDayRangePreviousMonth(shop.getId(), prev, i * 7);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByMonthRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInPreviousYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        int prev = 1;
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByMonthRangePreviousYEar(shop.getId(), prev, i);
            if (num == null) num = 0;
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getNumberOfOrderAnalystInAll(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int endYearRange = LocalDateTime.now().getYear();
        int beginYearRange = shop.getJoinTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .getYear();

        int prev = beginYearRange;
        for (int i = beginYearRange; i <= endYearRange; i += 1) {
            Integer num = orderRepository.getNumberOfOrderAnalystByYearRange(shop.getId(), prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i++) {
            Integer num = orderRepository.getRevenueAnalystByHourRange(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInPreviousDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByHourRangePreviousDay(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInPreviousWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 2;
        for (int i = 7 * weekRange + 1; i <= 7 * (weekRange + 1); i += 1) {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByDayRange(shop.getId(), prev, i * 7);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInPreviousMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByDayRangePreviousMonth(shop.getId(), prev, i * 7);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByMonthRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInPreviousYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByMonthRangePreviousYear(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getRevenueAnalystInAll(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int endYearRange = LocalDateTime.now().getYear();
        int beginYearRange = shop.getJoinTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .getYear();

        int prev = beginYearRange;
        for (int i = beginYearRange; i <= endYearRange; i += 1) {
            Integer num = orderRepository.getRevenueAnalystByYearRange(shop, prev, i);
            rs.add(num);
            prev = i;
        }

        return rs;
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

    public Integer getMaxPage(Shop shop, String kw)
    {
        var orders = orderRepository.findByShopAndIdContainingIgnoreCase(shop, kw);
        float div = orders.size() * 1.0f / 6;
        return ceil(div);
    }

    public Integer getMaxPage(Shop shop, OrderStatus status, String kw)
    {
        var orders = orderRepository.findByShopAndStatusAndIdContainingIgnoreCase(shop, status, kw);
        float div = orders.size() * 1.0f / 6;
        return ceil(div);
    }

    public Integer getNumOfOrder(Shop shop, String kw)
    {
        var orders = orderRepository.findByShopAndIdContainingIgnoreCase(shop, kw);
        return orders.size();
    }

    public Integer getNumOfOrder(Shop shop, OrderStatus status, String kw)
    {
        var orders = orderRepository.findByShopAndStatusAndIdContainingIgnoreCase(shop, status, kw);
        return orders.size();
    }

    public Integer getMaxPageShipper(OrderStatus status, String kw) {
        var orders =  orderRepository.findByStatusAndAndSpecialAndIdContainingIgnoreCase(status, false, kw);
        float div = orders.size() * 1.0f / 6;
        return ceil(div);
    }

    public List<Order> getByShopAndId(Shop shop, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());
        return orderRepository.findByShopAndIdContainingIgnoreCase(shop, kw, pageable);
    }

    public List<Order> getByShopAndStatusAndId(Shop shop, OrderStatus status, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());

        return orderRepository.findByShopAndStatusAndIdContainingIgnoreCase(shop, status, kw, pageable);
    }

    public List<Order> getByUserAndId(User user, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());
        return orderRepository.findByUserAndIdContainingIgnoreCase(user, kw, pageable);
    }

    public List<Order> getByUserAndStatusAndId(User user, OrderStatus status, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());
        return orderRepository.findByUserAndStatusAndIdContainingIgnoreCase(user, status, kw, pageable);
    }

    public List<Order> getByStatusAndNotSpecial(OrderStatus status, String kw, Integer page) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdTime").descending());
        return orderRepository.findByStatusAndAndSpecialAndIdContainingIgnoreCase(status, false, kw, pageable);
    }

    public String generateOrderId(LocalDateTime localDateTime) {
        String year = String.valueOf(localDateTime.getYear());
        String month = String.valueOf(localDateTime.getMonthValue());
        String date = String.valueOf(localDateTime.getDayOfMonth());
        return year + month + date;
    }

    public void init() {
        User user = (User) userService.loadUserByUsername("tranthienthanhbao@gmail.com");
        Shop shop = shopService.getShopById(2);
        Product p1 = productService.getProductById(22);
        Product p2 = productService.getProductById(25);
        Ward ward = Ward.builder()
                .id("1A0218")
                .name("Phường Tràng Tiền")
                .build();
        District district = District.builder()
                .id(1489)
                .name("Quận Hoàn Kiếm")
                .build();

        Province province = Province.builder()
                .id(201)
                .name("Hà Nội")
                .build();
        wardService.save(ward);
        districtService.save(district);
        provinceService.save(province);

        ReceiveInfo receiveInfo = ReceiveInfo.builder()
                ._default(true)
                .fullname("Trần Thiện Thanh Bảo")
                .phone("0914536728")
                .specific_address("Thảo Điền")
                .user(user)
                .ward(ward)
                .district(district)
                .province(province)
                .build();
        receiveInfoService.saveReceiveInfo(receiveInfo);

        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1).minusHours(16);
        Date date = Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());

        Order order = Order.builder()
                .id(generateOrderId(localDateTime) + 1)
                .user(user)
                .status(OrderStatus.COMPLETED)
                .payment("Cash On Delivery")
                .description(p1.getDescription().substring(0, 150))
                .receiveInfo(receiveInfo)
                .shop(shop)
                .createdTime(date)
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
                .id(generateOrderId(LocalDateTime.now()) + 1)
                .user(user)
                .status(OrderStatus.COMPLETED)
                .payment("Cash On Delivery")
                .receiveInfo(receiveInfo)
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
