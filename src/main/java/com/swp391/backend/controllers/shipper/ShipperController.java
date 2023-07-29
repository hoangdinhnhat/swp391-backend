package com.swp391.backend.controllers.shipper;

import com.swp391.backend.model.category.CategoryService;
import com.swp391.backend.model.counter.Counter;
import com.swp391.backend.model.counter.CounterService;
import com.swp391.backend.model.notification.Notification;
import com.swp391.backend.model.notification.NotificationService;
import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.order.OrderDTO;
import com.swp391.backend.model.order.OrderService;
import com.swp391.backend.model.order.OrderStatus;
import com.swp391.backend.model.product.Product;
import com.swp391.backend.model.product.ProductDTO;
import com.swp391.backend.model.product.ProductService;
import com.swp391.backend.model.productSale.ProductSale;
import com.swp391.backend.model.productSale.ProductSaleService;
import com.swp391.backend.model.receiveinfo.ReceiveInfoService;
import com.swp391.backend.model.report.Report;
import com.swp391.backend.model.report.ReportDTO;
import com.swp391.backend.model.report.ReportService;
import com.swp391.backend.model.settings.Setting;
import com.swp391.backend.model.settings.SettingService;
import com.swp391.backend.model.shop.Shop;
import com.swp391.backend.model.shop.ShopDTO;
import com.swp391.backend.model.shop.ShopService;
import com.swp391.backend.model.shopPackage.ShopPackageService;
import com.swp391.backend.model.user.User;
import com.swp391.backend.model.user.UserDTO;
import com.swp391.backend.model.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipper")
@PreAuthorize("hasAuthority('SHIPPING_UNIT')")
public class ShipperController {

    private final OrderService orderService;
    private final NotificationService notificationService;
    private final ProductSaleService productSaleService;
    private final CounterService counterService;
    private final ShopService shopService;

    @GetMapping("/max-page")
    public ResponseEntity<Integer> getMaxPage(@RequestParam("keyword") Optional<String> kw)
    {
        String keyword = kw.orElse("");
        int maxPage = orderService.getMaxPageShipper(OrderStatus.SHIPPING, keyword) + 1;
        return ResponseEntity.ok().body(maxPage);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrderShipping(
            @RequestParam("keyword") Optional<String> kw,
            @RequestParam("page") Optional<Integer> pg
    )
    {
        String keyword = kw.orElse("");
        Integer page = pg.orElse(1) - 1;
        List<OrderDTO> orders = orderService.getByStatusAndNotSpecial(OrderStatus.SHIPPING, keyword, page)
                .stream()
                .map(it -> it.toDto())
                .toList();
        return ResponseEntity.ok().body(orders);
    }

    @PostMapping("/order/special/process/{id}")
    public ResponseEntity<String> processSpecialOrder(
            @PathVariable("id") String orderId,
            @RequestParam("action") String action
    ) {

        Order order = orderService.getById(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = order.getUser();
        Shop shop = order.getShop();

        Notification notificationShop = Notification.builder()
                .imageUrl(user.getImageurl())
                .shop(shop)
                .createdAt(new Date())
                .read(false)
                .build();

        Notification notificationUser = Notification.builder()
                .imageUrl(shop.getShopImage())
                .user(user)
                .createdAt(new Date())
                .read(false)
                .build();


        if (action.equals("CONFIRM")) {
            notificationShop.setTitle("Delivery successful!");
            notificationShop.setContent(String.format("Order has been successfully delivered to customer %s!", user.getFirstname()));
            notificationShop.setRedirectUrl("/seller/portal/order/complete");

            notificationUser.setTitle("Delivery successful!");
            notificationUser.setContent("Your order has been successfully delivered to you. Thanks for your support!");
            notificationUser.setRedirectUrl("/purchase/complete");
            order.setStatus(OrderStatus.COMPLETED);
            Counter wallet = counterService.getById("WALLET");
            order.getOrderDetails().forEach(od -> {
                double differentPrice = od.getSellPrice() - od.getSoldPrice();
                wallet.setValue(wallet.getValue() - differentPrice);
                shop.setWallet(shop.getWallet() + differentPrice);
            });
            shopService.save(shop);
            counterService.save(wallet);
        }

        if (action.equals("REJECT")) {
            notificationShop.setTitle("Delivery failed!");
            notificationShop.setContent(String.format("Customer %s refused to receive the orders!", user.getFirstname()));
            notificationShop.setRedirectUrl("/seller/portal/order/cancel");

            notificationUser.setTitle("Delivery failed!");
            notificationUser.setContent("You have refused to accept the application. If there is any problem with the goods, we apologize!");
            notificationUser.setRedirectUrl("/purchase/cancel");
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderService.save(order);

        notificationService.save(notificationUser);
        notificationService.save(notificationShop);

        return ResponseEntity.ok().build();
    }
}
