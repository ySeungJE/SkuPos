package springFinal.POS.domain.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springFinal.POS.domain.payment.Payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer price;
    private String orderUid; // 주문 번호
    private String email;
    private String address;
    private String userName;
    private String title;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ElementCollection
    @MapKeyColumn(name = "itemName")
    @Column(name = "saleNumber")
    @Builder.Default
    public Map<String, Integer> itemData = new HashMap<>();

    @ElementCollection
    @MapKeyColumn(name = "dateKey")
    @Column(name = "dateData")
    @Builder.Default
    public Map<String, String> saleDate = new HashMap<>();

    public void updateSaleDate(String day, String week, String month) {
        this.saleDate.put("day", day);
        this.saleDate.put("week",week);
        this.saleDate.put("month",month);
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
