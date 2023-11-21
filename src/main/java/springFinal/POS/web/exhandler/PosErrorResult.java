package springFinal.POS.web.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@Data
@AllArgsConstructor
@ToString
public class PosErrorResult {
    private String code;
    private String message;
}