package springFinal.POS.domain.order.service;


import springFinal.POS.domain.order.Order;
import springFinal.POS.domain.payment.PaymentStatus;

import java.util.List;

public interface OrderService {
    Order itemOrder(String userName, Long price, List<String> itemList); // 자동 주문

    Order findById(Long id);

    void changeState(PaymentStatus paymentStatus);

    List<Order> findAll();
}
