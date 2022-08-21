package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Service
public class ItemServiceImp implements ItemService {

    ItemDao itemDao;

    public ItemServiceImp(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        return toItemDto(itemDao.addItem(userId, toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        return toItemDto(itemDao.updateItem(itemId, userId, toItem(itemDto)));
    }

    @Override
    public String deleteItem(long itemId, long userId) {
        return itemDao.deleteItem(itemId, userId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        return toItemDto(itemDao.getItem(itemId));
    }

    @Override
    public List<ItemDto> getItemsOwner(long userId) {
        return itemDao.getItemsOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemDao.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
