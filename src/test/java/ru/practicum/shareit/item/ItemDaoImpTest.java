package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDaoImp;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemDaoImpTest {
    private static UserDaoImp userDaoImp = new UserDaoImp();
    private static UserDto userDto;
    private static ItemDaoImp itemDaoImp = new ItemDaoImp(userDaoImp);
    private static ItemDto itemDto;

    @BeforeAll
    private static void setup() {
        userDto = new UserDto(
                null, "name", "user@user.com"
        );
        userDaoImp.addUser(userDto);
        itemDto = new ItemDto(
          null, "item", "description", true, null
        );
        itemDaoImp.addItem(1L, itemDto);
    }

    @Test
    void addItem() {
        Assertions.assertEquals(itemDaoImp.getItemsOwner(1L).size(), 1);
    }

    @Test
    void updateItem() {
        ItemDto itemDto2 = new ItemDto(
                null, null, null, false, null
        );
        itemDaoImp.updateItem(1L,1L, itemDto2);
        Assertions.assertFalse(itemDaoImp.getItemsOwner(1).get(0).getAvailable());
    }

    @Test
    void searchItem() {
        Assertions.assertEquals(itemDaoImp.searchItems("item").get(0).getName(), "item");
    }

    @Test
    void deleteItem() {
        itemDaoImp.deleteItem(1L, 1L);
        Assertions.assertEquals(itemDaoImp.getItemsOwner(1L).size(), 0);
    }

    @Test
    void addItemByNotRegisterUser(){
        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            ItemDto itemDto2 = new ItemDto(
                    null, "item2", "description2", true, null
            );
            itemDaoImp.addItem(4L, itemDto2);
        });
        Assertions.assertEquals("Пользователя с ID 4 не найдено!", thrown.getMessage());
    }
}