package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item addItem(long userId, Item item);

    Item updateItem(long itemId, long userId, Item item);

    String deleteItem(long itemId, long userId);

    Item getItem(long itemId);

    List<Item> getItemsOwner(long userId);

    List<Item> searchItems(String text);
}
