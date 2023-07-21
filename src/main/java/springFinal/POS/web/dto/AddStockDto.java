package springFinal.POS.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddStockDto {
    private List<Integer> itemNumber;
}
