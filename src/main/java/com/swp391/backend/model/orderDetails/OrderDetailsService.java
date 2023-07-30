package com.swp391.backend.model.orderDetails;

import com.swp391.backend.model.counter.Counter;
import com.swp391.backend.model.counter.CounterService;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderRepository;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ProductSaleService productSaleService;
    private final CounterService counterService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderDetails save(OrderDetails orderDetails) {
        Order order = orderDetails.getOrder();
        Product product = orderDetails.getProduct();
        double price = product.getPrice();

        ProductSale sale = productSaleService.findProductInSale(product);
        orderDetails.setSellPrice(price);

        if (sale != null && sale.getSaleQuantity() > sale.getSold()) {
            int percent = sale.getSaleEvent().getPercent();
            price = price * (1 - percent * 1.0 / 100);
            price = Math.round(price * 100) * 1.0 / 100;
        }

        orderDetails.setSoldPrice(price);
        orderDetails.setFeedbacked(false);

        order.setSellPrice(order.getSellPrice() + orderDetails.getSellPrice() * orderDetails.getQuantity());
        order.setSoldPrice(order.getSoldPrice() + orderDetails.getSoldPrice() * orderDetails.getQuantity());
        orderRepository.save(order);
        return orderDetailsRepository.save(orderDetails);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderDetails saveRefund(OrderDetails orderDetails) {
        Order order = orderDetails.getOrder();

        order.setSoldPrice(order.getSoldPrice() + orderDetails.getSoldPrice() * orderDetails.getQuantity());
        order.setSellPrice(order.getSellPrice() + orderDetails.getSellPrice() * orderDetails.getQuantity());
        orderRepository.save(order);
        return orderDetailsRepository.save(orderDetails);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(OrderDetails orderDetails) {
        Order order = orderDetails.getOrder();

        order.setSoldPrice(order.getSoldPrice() - orderDetails.getSoldPrice() * orderDetails.getQuantity());
        order.setSellPrice(order.getSellPrice() - orderDetails.getSellPrice() * orderDetails.getQuantity());
        orderRepository.save(order);
        orderDetailsRepository.delete(orderDetails);
    }

    public List<Integer> getNumberOfSoldProductAnalystInDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByHourRange(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInPreviousDay(Shop shop) {
        List<Integer> rs = new ArrayList<>();
        int hourRange = Math.round(Math.round(Math.floor(LocalDateTime.now().getHour() * 1.0 / 6))) + 1;
        int prev = 0;
        for (int i = 1; i <= hourRange; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByHourRangePreviousDay(shop.getId(), prev, i * 6 - 1);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 6;
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 1;
        for (int i = 7 * weekRange + 1; i <= currentDay; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByDayRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInPreviousWeek(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7))) - 2;
        for (int i = 7 * weekRange + 1; i <= 7 * (weekRange + 1); i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByDayRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByDayRange(shop.getId(), prev, i * 7);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInPreviousMonth(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int currentDay = LocalDateTime.now().getDayOfMonth();
        int weekRange = Math.round(Math.round(Math.ceil(currentDay * 1.0 / 7)));
        int prev = 1;
        for (int i = 1; i <= weekRange; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByDayRangePreviousMonth(shop.getId(), prev, i * 7);
            if (num == null) num = 0;
            rs.add(num);
            prev = i * 7 + 1;
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByMonthRange(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public List<Integer> getNumberOfSoldProductAnalystInPreviousYear(Shop shop) {
        List<Integer> rs = new ArrayList<>();

        int monthRange = LocalDateTime.now().getMonthValue();
        for (int i = 1; i <= monthRange; i += 1) {
            Integer num = orderDetailsRepository.getNumberOfSoldProductAnalystByMonthRangePreviousYear(shop.getId(), i, i);
            if (num == null) num = 0;
            rs.add(num);
        }

        return rs;
    }

    public void setFeedbacked(OrderDetails orderDetails) {
        orderDetails.setFeedbacked(true);
        orderDetailsRepository.save(orderDetails);
    }

    public OrderDetails getByOrderAndProduct(Order order, Product product) {
        return orderDetailsRepository.findByOrderAndProduct(order, product);
    }

    public List<OrderDetails> getByOrder(Order order) {
        return orderDetailsRepository.findByOrder(order);
    }

    public OrderDetails getById(OrderDetailsId id)
    {
        return orderDetailsRepository.findById(id).orElse(null);
    }
}
