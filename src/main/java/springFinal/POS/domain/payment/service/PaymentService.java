package springFinal.POS.domain.payment.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import springFinal.POS.domain.User.User;
import springFinal.POS.web.dto.PaymentCallbackRequest;
import springFinal.POS.web.dto.RequestPayDto;
import springFinal.POS.web.dto.SaleData;

public interface PaymentService {
    // 결제 요청 데이터 조회
    RequestPayDto findRequestDto(String orderUid);

    RequestPayDto findRequestDto2(SaleData saleData, User user);

    // 결제 검증(콜백)
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);
}
