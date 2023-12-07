package springFinal.POS.domain.Pos;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Slf4j
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
        log.info("업데이트 될 금액은 {}", summary);
        balance += summary;

//        if (dayTurnover.containsKey(day)) {
            dayTurnover.put(day, dayTurnover.getOrDefault(day, 0) + summary);
//        } else {
//            dayTurnover.put(day, summary);
//        }

//        if (weekTurnover.containsKey(week)) {
            weekTurnover.put(week, weekTurnover.getOrDefault(week, 0) + summary);
//        } else {
//            weekTurnover.put(week, summary);
//        }

//        if (monthTurnover.containsKey(month)) {
            monthTurnover.put(month, monthTurnover.getOrDefault(month, 0) + summary);
//        } else {
//            monthTurnover.put(month, summary);
//        }
    }

    /**
     * 입고 날짜 저장 리스트
     * @param day
     */
    public void addStockDay(String day) {
        this.stockDay.add(day);
    }

}
