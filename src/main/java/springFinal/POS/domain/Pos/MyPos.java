package springFinal.POS.domain.Pos;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class MyPos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mypos_id")
    private Long id;
    private Integer balance;

    @ElementCollection
    @Builder.Default
    public List<String> stockDay = new ArrayList<>();
    @ElementCollection
    @MapKeyColumn(name = "day_key")
    @Column(name = "day_turnover")
    @Builder.Default
    private Map<String, Integer> dayTurnover = new LinkedHashMap<>();
    @ElementCollection
    @MapKeyColumn(name = "week_key")
    @Column(name = "week_turnover")
    @Builder.Default
    private Map<String, Integer> weekTurnover = new LinkedHashMap<>();
    @ElementCollection
    @MapKeyColumn(name = "month_key")
    @Column(name = "month_turnover")
    @Builder.Default
    private Map<String, Integer> monthTurnover = new LinkedHashMap<>();

    //== 비즈니스 메서드 ==//

    /**
     * 일간, 주간, 월간 총 매출의 계산 처리
     * @param summary
     * @param day
     * @param week
     * @param month
     */
    public void updateTurnover(Integer summary, String day, String week, String month) {
        balance += summary;

        if (dayTurnover.containsKey(day)) {
            dayTurnover.put(day, dayTurnover.get(day) + summary);
        } else {
            dayTurnover.put(day, summary);
        }

        if (weekTurnover.containsKey(week)) {
            weekTurnover.put(week, weekTurnover.get(week) + summary);
        } else {
            weekTurnover.put(week, summary);
        }

        if (monthTurnover.containsKey(month)) {
            monthTurnover.put(month, monthTurnover.get(month) + summary);
        } else {
            monthTurnover.put(month, summary);
        }
    }
    public void addStockDay(String day) {
        this.stockDay.add(day);
    }

}
