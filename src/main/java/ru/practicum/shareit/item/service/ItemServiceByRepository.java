package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Slf4j
@Service("ItemServiceByRepository")
public class ItemServiceByRepository implements ItemService{

    ItemRepository itemRepository;

    public ItemServiceByRepository(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        return toItemDto(itemRepository.save(toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        return /*toItemDto(itemDao.updateItem(itemId, userId, toItem(itemDto)))*/ null;
    }

    @Override
    public String deleteItem(long itemId, long userId) {
        if (itemRepository.findById(itemId).get().getOwner() == userId) {
            itemRepository.deleteById(itemId);
        } else {
            throw new ValidationException("У пользователя нет прав удаления объекта");
        }
        return "Объект ID: " + itemId + ", удален";
    }

    @Override
    public ItemDto getItem(long itemId) {
        return toItemDto(itemRepository.findById(itemId).get());
    }

    @Override
    public List<ItemDto> getItemsOwner(long userId) {
        return /*itemDao.getItemsOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());*/ null;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return /*itemDao.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());*/ null;
    }
}
