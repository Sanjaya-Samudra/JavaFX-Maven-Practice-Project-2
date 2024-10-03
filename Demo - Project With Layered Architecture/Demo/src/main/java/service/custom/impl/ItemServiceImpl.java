package service.custom.impl;

import javafx.collections.ObservableList;
import dto.Item;
import dto.OrderDetail;
import service.custom.ItemService;

import java.util.List;

public class ItemServiceImpl implements ItemService {
    @Override
    public boolean addItem(Item item) {
        return false;
    }

    @Override
    public ObservableList<Item> getAllItems() {
        return null;
    }

    @Override
    public boolean updateItem(Item item) {
        return false;
    }

    @Override
    public boolean deleteItem(String id) {
        return false;
    }

    @Override
    public Item searchItem(String id) {
        return null;
    }

    @Override
    public boolean updateStock(List<OrderDetail> orderDetails) {
        return false;
    }
}