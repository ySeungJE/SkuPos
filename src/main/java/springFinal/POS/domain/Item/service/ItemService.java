package springFinal.POS.domain.Item.service;

import springFinal.POS.domain.Item.Item;
import springFinal.POS.web.dto.ItemsData;

import java.util.List;

public interface ItemService {
    public void addStock(Item item, Integer stock, String date);
    public Item save(Item item);
    public List<Item> findAll();
    public void itemSale(List<ItemsData> itemDataList);
    void initItem();
}
