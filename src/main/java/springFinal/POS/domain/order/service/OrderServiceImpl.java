package springFinal.POS.domain.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springFinal.POS.domain.User.User;
import springFinal.POS.domain.order.Order;
import springFinal.POS.domain.order.repository.OrderRepository;
import springFinal.POS.domain.payment.Payment;
import springFinal.POS.domain.payment.PaymentStatus;
import springFinal.POS.domain.payment.repository.PaymentRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public Order itemOrder(String userName, Long price, List<String> itemList) {

        // 임시 결제내역 생성
        Payment payment = Payment.builder()
                .price(price)
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);

        // 주문 생성
        Order order = Order.builder()
                .price(price)
                .address("임시 주소")
                .email("dbstmdwp98@naver.com")
                .itemList(itemList)
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .build();

        return orderRepository.save(order);
    }
}