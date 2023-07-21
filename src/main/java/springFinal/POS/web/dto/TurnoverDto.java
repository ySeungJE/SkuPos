package springFinal.POS.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TurnoverDto {
    private String date;
    private Integer price;
}
