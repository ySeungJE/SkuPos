package springFinal.POS.domain.Item.service;

import springFinal.POS.domain.Item.Item;
import springFinal.POS.web.dto.ItemsDataDto;

import java.util.List;

import static springFinal.POS.domain.Item.repository.ItemRepository.*;

public interface ItemService {
    Item save(Item item);
    Item findByName(String itemName);
    List<Item> findAll();
    List<ItemMapping> findAllName();
    void addStock(String name, Integer stock, String date);
    void  itemSale(List<ItemsDataDto> itemDataList);
    void deleteItem(String itemName);
    void recover(Item item);
}
