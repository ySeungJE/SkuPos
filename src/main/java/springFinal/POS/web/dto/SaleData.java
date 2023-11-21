package springFinal.POS.web.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SaleData {
    private Integer summary;
    private List<ItemsDataDto> itemDataList;
}
