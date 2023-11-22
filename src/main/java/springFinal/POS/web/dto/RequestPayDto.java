package springFinal.POS.web.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor @Builder
public class RequestPayDto {
    private String orderUid;
    private String itemName;
    private String buyerName;
    private Long paymentPrice;
    private String buyerEmail;
    private String buyerAddress;
}