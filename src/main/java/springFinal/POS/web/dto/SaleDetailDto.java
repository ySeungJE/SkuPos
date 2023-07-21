package springFinal.POS.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaleDetailDto {
    private String name;
    private Integer number;
    private Integer price;
}
