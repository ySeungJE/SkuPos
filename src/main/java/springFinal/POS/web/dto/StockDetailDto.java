package springFinal.POS.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDetailDto {
    private Boolean exist;
    private String name;
    private Integer number;
}
