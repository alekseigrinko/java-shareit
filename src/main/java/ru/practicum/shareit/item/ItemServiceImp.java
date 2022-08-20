package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service("ItemServiceImp")
public class ItemServiceImp implements ItemService {

    ItemDao itemDao;

    public ItemServiceImp(@Qualifier("ItemDaoImp") ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        return itemDao.addItem(userId, itemDto);
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        return itemDao.updateItem(itemId, userId, itemDto);
    }

    @Override
    public String deleteItem(long itemId, long userId) {
        return itemDao.deleteItem(itemId, userId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return itemDao.getItem(itemId);
    }

    @Override
    public List<ItemDto> getItemsOwner(long userId) {
        return itemDao.getItemsOwner(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemDao.searchItems(text);
    }
}
