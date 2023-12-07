package springFinal.POS.domain.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springFinal.POS.domain.User.User;
import springFinal.POS.domain.order.Order;
import springFinal.POS.domain.order.repository.OrderRepository;
import springFinal.POS.domain.payment.PaymentStatus;
import springFinal.POS.domain.payment.repository.PaymentRepository;
import springFinal.POS.web.dto.PaymentCallbackRequest;
import springFinal.POS.web.dto.RequestPayDto;
import springFinal.POS.web.dto.SaleData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    @Override
    public RequestPayDto findRequestDto(String orderUid) {

        Order order = orderRepository.findByOrderUid(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));

        return RequestPayDto.builder()
                .buyerName(order.getUserName())
                .buyerEmail(order.getEmail())
                .buyerAddress(order.getAddress())
                .paymentPrice(order.getPrice())
                .itemName("testName")
                .orderUid(order.getOrderUid())
                .build();
    }
    @Override
    public RequestPayDto findRequestDto2(SaleData saleData, User user) {

        return RequestPayDto.builder()
                .buyerName(user.getName())
                .buyerEmail("dbstmdwp98@naver.com")
                .buyerAddress("임시 주소")
                .paymentPrice(saleData.getSummary())
                .itemName("testName")
                .orderUid(UUID.randomUUID().toString())
                .build();
    }

    @Override
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {

        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            Order order = orderRepository.findByOrderUid(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            Integer price = order.getPayment().getPrice();
            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if(iamportPrice != price) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }

            // 결제 상태 변경
            order.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
