package springFinal.POS.domain.Item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springFinal.POS.domain.Item.Item;
import springFinal.POS.domain.Item.repository.ItemRepository;
import springFinal.POS.web.dto.ItemsData;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    @Override
    public void addStock(Item item, Integer stock, String date) {
        item.addStock(stock, date);
    }
    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }
    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
    @Override
    public void itemSale(List<ItemsData> itemDataList) {

        DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");

        String day = LocalDateTime.now().format(dayFormat);
        String month = LocalDateTime.now().format(monthFormat);

        String[] dateArray = day.split("-");

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(dateArray[0]), (Integer.parseInt(dateArray[1]) -1), Integer.parseInt(dateArray[2])-1);

        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();

        cal.add(Calendar.DAY_OF_MONTH, - dayOfWeek);
        //해당 주차의 첫 일자
        String stDt = formatter.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 6);
        //해당 주차의 마지막일자
        String edDt = formatter.format(cal.getTime());

        String week = stDt + " ~ " + edDt;

        itemDataList.forEach(itemData -> {
            Item item = itemRepository.findByName(itemData.getItemName()).orElse(null);
            item.itemSale(itemData.getSaleNumber(), day, week, month);
        });
    }
    @Override
    public void initItem() {
        List<Item> items = new ArrayList<>();
        items.add(save(Item.builder().name("크림빵").price(1500).salesVolume(0).stock(0).build()));
        items.add(save(Item.builder().name("삼각김밥").price(1200).salesVolume(0).stock(0).build()));
        items.add(save(Item.builder().name("미니족발").price(7500).salesVolume(0).stock(0).build()));
        items.add(save(Item.builder().name("포카리스웨트").price(2500).salesVolume(0).stock(0).build()));
        items.add(save(Item.builder().name("참이슬").price(1700).salesVolume(0).stock(0).build()));
        items.add(save(Item.builder().name("테라피쳐").price(3500).salesVolume(0).stock(0).build()));
    }
}
