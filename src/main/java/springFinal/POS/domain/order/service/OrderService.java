package springFinal.POS.domain.order.service;


import springFinal.POS.domain.order.Order;

import java.util.List;

public interface OrderService {
    Order itemOrder(String userName, Long price, List<String> itemList); // 자동 주문
}
