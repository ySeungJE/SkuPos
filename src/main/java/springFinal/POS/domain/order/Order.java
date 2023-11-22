package springFinal.POS.domain.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springFinal.POS.domain.User.User;
import springFinal.POS.domain.payment.Payment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    private String orderUid; // 주문 번호
    private String email;
    private String address;
    private String userName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ElementCollection
    @Builder.Default
    public List<String> itemList = new ArrayList<>(); //여기에는 모든 아이템을 담되, 결제 시스템에 넣을 때는 '첫번째 아이템 외 3개'로
}
