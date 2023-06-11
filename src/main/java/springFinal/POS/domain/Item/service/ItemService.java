package springFinal.POS.domain.Item.service;

import springFinal.POS.domain.Item.Item;
import springFinal.POS.web.dto.ItemsDataDto;

import java.util.List;

import static springFinal.POS.domain.Item.repository.ItemRepository.*;

public interface ItemService {
    void addStock(String name, Integer stock, String date);
    Item save(Item item);
    List<Item> findAll();
    List<ItemMapping> findAllName();
    void itemSale(List<ItemsDataDto> itemDataList);
    void deleteItem(String itemName);
    Item findByName(String itemName);

    void recover(Item item);
}
