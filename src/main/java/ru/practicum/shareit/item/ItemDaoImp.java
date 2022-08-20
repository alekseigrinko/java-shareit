package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("ItemDaoImp")
@Slf4j
public class ItemDaoImp implements ItemDao {
    private HashMap<Long, Item> itemMap = new HashMap<>();
    private Long id = 0L;
    private ItemMapper itemMapper = new ItemMapper();
    private UserDao userDao;

    public ItemDaoImp(UserDao userDao) {
        this.userDao = userDao;
    }

    private Long createId() {
        id++;
        return id;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        userDao.checkUser(userId);
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userId);
        item.setId(createId());
        itemMap.put(item.getId(), item);
        log.debug("Добавлен объект :" + item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        Item item = itemMapper.toItem(itemDto);
        item.setId(itemId);
        itemMap.put(item.getId(), item);
        log.debug("Обновлен объект :" + item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public String deleteItem(long itemId, long userId) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        itemMap.remove(itemId);
        log.debug("Удален объект ID: " + itemId);
        return "Удален объект ID: " + itemId;
    }

    @Override
    public ItemDto getItem(long itemId) {
        checkItem(itemId);
        log.warn("предоставлена информация по объекту ID: " + itemId);
        return itemMapper.toItemDto(itemMap.get(itemId));
    }

    @Override
    public List<ItemDto> getItemsOwner(long userId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getOwner() == userId) {
                itemDtoList.add(itemMapper.toItemDto(item));
            }
        }
        log.warn("Выведен список объектов пользователя ID: " + userId);
        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getName().contains(text) || item.getDescription().contains(text)) {
                itemDtoList.add(itemMapper.toItemDto(item));
            }
        }
        log.warn("Выведен список объектов согласно условию поиска: " + text);
        return itemDtoList;
    }

    private void checkItem(long itemId) {
        if (!itemMap.containsKey(itemId)) {
            log.warn("Объекта с ID " + itemId + " не найдено!");
            throw new ObjectNotFoundException("Объекта с ID " + itemId + " не найдено!");
        }
    }

    private void checkItemByUser(long itemId, long userId) {
        if (itemMap.get(itemId).getOwner() != userId) {
            log.warn("Право редактирования объекта не подтверждено!");
            throw new ObjectNotFoundException("Право редактирования объекта не подтверждено!");
        }
    }
}
