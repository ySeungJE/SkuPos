package springFinal.POS.web.dto;

import jakarta.validation.constraints.NegativeOrZero;
import lombok.Data;

@Data
public class ItemsDataDto {
    private String itemName;

    private Integer saleNumber;

}
