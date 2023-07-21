package springFinal.POS.web.dto;

import lombok.Data;

@Data
public class ItemAddDto {
    private String name;
    private Integer price;
    private Integer stock;
}
