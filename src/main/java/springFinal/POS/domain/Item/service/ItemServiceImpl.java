package springFinal.POS.domain.Item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springFinal.POS.domain.Item.Item;
import springFinal.POS.domain.Item.repository.ItemRepository;
import springFinal.POS.domain.order.Order;
import springFinal.POS.domain.order.repository.OrderRepository;
import springFinal.POS.domain.order.service.OrderService;
import springFinal.POS.web.dto.ItemsDataDto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static springFinal.POS.domain.Item.repository.ItemRepository.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    @Override
    public void addStock(String name, Integer stock, String date) {
        findByName(name).addStock(stock, date);
    }
    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }
    @Override
    public void deleteItem(String itemName) {
        itemRepository.findByName(itemName).orElse(null).deleteIt();
    }
    @Override
    public void recover(Item item) {
        item.recover();
    }

    @Override
    public Item findByName(String itemName) {
        return itemRepository.findByName(itemName).orElse(null);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
    @Override
    public List<ItemMapping> findAllName() {
        return itemRepository.findAllBy(ItemMapping.class);
    }
    @Override
    public void itemSale(List<ItemsDataDto> itemDataList, Long orderId) {

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

        Order order = orderRepository.findById(orderId).orElse(null);

        order.updateSaleDate(day, week, month);

        log.info("{} {} {}", day, week, month);

        // 여기서 map<날짜, 개수>을 생성해서 넣어주자. 통째로 넣는게 안되면 entry 반복으로 넣어주면 그만
        itemDataList.forEach(itemData -> {
            Item item = itemRepository.findByName(itemData.getItemName()).orElse(null);
            item.itemSale(itemData.getSaleNumber(), day, week, month);
        });
    }
}
