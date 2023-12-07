package springFinal.POS.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCallbackRequest {
    private String paymentUid; // 결제 고유 번호
    private String orderUid; // 주문 고유 번호
    private Integer summary;
    private List<ItemsDataDto> itemDataList;

}