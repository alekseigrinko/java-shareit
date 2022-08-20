package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemDao {
    ItemDto addItem (long userId, ItemDto itemDto);
    ItemDto updateItem (long itemId, long userId, ItemDto itemDto);
    String deleteItem(long itemId, long userId);
    ItemDto getItem(long itemId);
    List<ItemDto> getItemsOwner(long userId);
    List<ItemDto> searchItems(String text);


}