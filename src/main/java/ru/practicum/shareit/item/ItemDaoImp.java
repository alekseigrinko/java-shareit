package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Repository
@Slf4j
public class ItemDaoImp implements ItemDao {
    private HashMap<Long, Item> itemMap = new HashMap<>();
    private Long id = 0L;
    private UserDao userDao;

    public ItemDaoImp(UserDao userDao) {
        this.userDao = userDao;
    }

    private Long createId() {
        id++; //если убрать в return не сохраняется в памяти сложение
        return id;
    }

    @Override
    public Item addItem(long userId, Item item) {
        userDao.checkUser(userId);
        item.setOwner(userId);
        item.setId(createId());
        itemMap.put(item.getId(), item);
        log.debug("Добавлен объект :" + item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, long userId, Item item) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        Item itemInMemory = itemMap.get(itemId);
        if (item.getName() != null) {
            itemInMemory.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInMemory.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemInMemory.setAvailable(item.getAvailable());
        }
        itemMap.put(itemInMemory.getId(), itemInMemory);
        log.debug("Обновлен объект :" + item);
        return itemInMemory;
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
    public Item getItem(long itemId) {
        checkItem(itemId);
        log.warn("предоставлена информация по объекту ID: " + itemId);
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> getItemsOwner(long userId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if (item.getOwner() == userId) {
                itemList.add(item);
            }
        }
        log.warn("Выведен список объектов пользователя ID: " + userId);
        return itemList;
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            log.debug("Условие поиска не задано");
            return new ArrayList<>();
        }
        List<Item> itemList = new ArrayList<>();
        for (Item item : itemMap.values()) {
            if ((item.getAvailable()) && ((item.getName().toLowerCase(Locale.ROOT).contains(text))
                    || item.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))) {
                itemList.add(item);
            }
        }
        log.warn("Выведен список объектов согласно условию поиска: " + text);
        return itemList;
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
