package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithComment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForReturn;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;

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
    public ItemDtoWithComment getItem(long itemId, long userId) {
        return /*toItemDto(itemDao.getItem(itemId))*/null;
    }

    @Override
    public List<ItemDtoForReturn> getItemsOwner(long userId) {
        return /*itemDao.getItemsOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList())*/ null;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemDao.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, long userId, long itemId) {
        return null;
    }
}
