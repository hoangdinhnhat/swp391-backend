package com.swp391.backend.model.orderDetails;

import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderRepository;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderRepository orderRepository;
    private final ProductSaleService productSaleService;

    @Transactional
    public OrderDetails save(OrderDetails orderDetails)
    {
        Order order = orderDetails.getOrder();
        Product product = orderDetails.getProduct();
        ProductSale sale = productSaleService.findProductInSale(product);

        double price = product.getPrice();

        if (sale != null)
        {
            int percent = sale.getSaleEvent().getPercent();
            price = price * (1 - percent * 1.0 / 100);
            price = Math.round(price * 100) / 100;
        }

        price *= orderDetails.getQuantity();

        order.setTotalPrice(order.getTotalPrice() + price);
        orderRepository.save(order);
        return orderDetailsRepository.save(orderDetails);
    }

}
