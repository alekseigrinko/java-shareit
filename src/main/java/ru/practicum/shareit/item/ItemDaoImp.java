package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;

@Repository
@Slf4j
public class ItemDaoImp implements ItemDao {
    private HashMap<Long, Item> itemMap = new HashMap<>();
    private Long id = 0L;

    private Long createId() {
        id++;
        return id;
    }

}
