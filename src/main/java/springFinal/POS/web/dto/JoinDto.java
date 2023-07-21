package springFinal.POS.web.dto;

import lombok.Data;

@Data
public class JoinDto {
    private String name;
    private String loginId;
    private String password;
    private Boolean manager;
}
