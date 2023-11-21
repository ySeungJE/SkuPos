package springFinal.POS.domain.Item;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import springFinal.POS.web.dto.ItemAddDto;

import java.util.HashMap;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer price;
    @PositiveOrZero
    private Integer stock;
    private Boolean exist;
    @ElementCollection
    @MapKeyColumn(name = "date_key")
    @Column(name = "stock")
    @Builder.Default
    private Map<String, Integer> dateStock = new HashMap<>();
    @ElementCollection
    @MapKeyColumn(name = "day_key")
    @Column(name = "day_volume")
    @Builder.Default
    private Map<String, Integer> daySales = new HashMap<>();
    @ElementCollection
    @MapKeyColumn(name = "week_key")
    @Column(name = "week_volume")
    @Builder.Default
    private Map<String, Integer> weekSales = new HashMap<>();
    @ElementCollection
    @MapKeyColumn(name = "month_key")
    @Column(name = "month_volume")
    @Builder.Default
    private Map<String, Integer> monthSales = new HashMap<>();

    //== 생성 메서드 ==//
    public static Item createItem(ItemAddDto dto) {
        return Item.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(0)
                .exist(true).build();
    }

    //== 비즈니스 메서드 ==//
    /**
     * 제품 입고 메서드
     * @param stock
     */
    public void addStock(Integer stock, String date) {
        this.stock += stock;
        if (dateStock.containsKey(date)) {
            dateStock.put(date, dateStock.get(date) + stock);
        } else {
            dateStock.put(date, stock);
        }
    }

    /**
     * 제품 판매 메서드
     * @param volume
     */
    public void itemSale(Integer volume, String day, String week, String month) {
        this.stock -= volume;

        if (daySales.containsKey(day)) {
            daySales.put(day, daySales.get(day) + volume);
        } else {
            daySales.put(day, volume);
        }
        
        if (weekSales.containsKey(week)) {
            weekSales.put(week, weekSales.get(week) + volume);
        } else {
            weekSales.put(week, volume);
        }
        
        if (monthSales.containsKey(month)) {
            monthSales.put(month, monthSales.get(month) + volume);
        } else {
            monthSales.put(month, volume);
        }
    }
    public void deleteIt() {
        this.exist=false;
    }
    public void recover() {
        this.exist=true;
    }
}
