package springFinal.POS.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTO {
    private String result;
    private RequestPayDto payDto;

    public MessageDTO(String result) {
        this.result = result;
    }
}
