package springFinal.POS.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaleData {
    private Integer summary;
    private List<ItemsDataDto> itemDataList;
}
