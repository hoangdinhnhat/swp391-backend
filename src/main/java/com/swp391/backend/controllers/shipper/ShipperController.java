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

import java.util.*;

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
    private final UserService userService;

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
        List<OrderDTO> result = new ArrayList<>();
        List<OrderDTO> orders = orderService.getByStatusAndNotSpecial(OrderStatus.SHIPPING, keyword, page)
                .stream()
                .map(it -> it.toDto())
                .toList();

        List<OrderDTO> orders2 = orderService.getByStatusAndNotSpecial(OrderStatus.REFUND, keyword, page)
                .stream()
                .map(it -> it.toDto())
                .toList();

        for (OrderDTO order : orders)
        {
            result.add(order);
        }

        for (OrderDTO order : orders2)
        {
            result.add(order);
        }

        return ResponseEntity.ok().body(result);
    }

    public void liquidityForShop(Order order)
    {
        Shop shop = order.getShop();
        Counter wallet = counterService.getById("WALLET");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                shop.setWallet(shop.getWallet() + order.getSellPrice());
                shopService.save(shop);
                wallet.setValue(wallet.getValue() - order.getSellPrice());

                counterService.save(wallet);
                order.setReported(true);
                orderService.save(order);
            }
        };

        long delay = 3 * 24 * 60 * 60 * 1000;
        Timer timer = new Timer("Timer");
        timer.schedule(timerTask, delay);
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
            if (order.getStatus().equals(OrderStatus.REFUND))
            {
                Counter counter = counterService.getById("WALLET");
                user.setWallet(user.getWallet() + order.getSoldPrice() + order.getShippingFee());
                counter.setValue(counter.getValue() - order.getSoldPrice() - order.getShippingFee());

                userService.save(user);
                counterService.save(counter);
            }else {
                order.setStatus(OrderStatus.COMPLETED);
                liquidityForShop(order);
            }
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
