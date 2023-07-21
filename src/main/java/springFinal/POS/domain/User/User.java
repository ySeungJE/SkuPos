package springFinal.POS.domain.User;


import jakarta.persistence.*;
import lombok.*;
import springFinal.POS.web.dto.JoinDto;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    @Column(unique = true)
    private String loginId;
    private String password;
    private Boolean manager;

    //== 생성 메서드 ==//
    public static User createUser(JoinDto joinDto) {
        return User.builder()
                .loginId(joinDto.getLoginId())
                .password(joinDto.getPassword())
                .name(joinDto.getName())
                .manager(joinDto.getManager())
                .build();
    }
}
