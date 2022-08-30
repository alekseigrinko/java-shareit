package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Slf4j
@Service("ItemServiceByRepository")
public class ItemServiceByRepository implements ItemService {

    final private ItemRepository itemRepository;
    final private UserRepository userRepository;

    public ItemServiceByRepository(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkUser(userId);
        itemDto.setOwner(userId);
        return toItemDto(itemRepository.save(toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        Item itemInMemory = itemRepository.findById(itemId).get();
        if (itemDto.getName() != null) {
            itemInMemory.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemInMemory.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemInMemory.setAvailable(itemDto.getAvailable());
        }
        log.debug("Обновлен объект :" + itemInMemory);
        return toItemDto(itemRepository.save(itemInMemory));
    }

    @Override
    public String deleteItem(long itemId, long userId) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        itemRepository.deleteById(itemId);
        return "Объект ID: " + itemId + ", удален";
    }

    @Override
    public ItemDto getItem(long itemId) {
        checkItem(itemId);
        log.warn("предоставлена информация по объекту ID: " + itemId);
        return toItemDto(itemRepository.findById(itemId).get());
    }

    @Override
    public List<ItemDto> getItemsOwner(long userId) {
        checkUser(userId);
        log.warn("Выведен список объектов пользователя ID: " + userId);
        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchItem(text.toLowerCase()).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkItem(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.warn("Объекта с ID " + itemId + " не найдено!");
            throw new ObjectNotFoundException("Объекта с ID " + itemId + " не найдено!");
        }
    }

    private void checkItemByUser(long itemId, long userId) {
        if (itemRepository.findById(itemId).get().getOwner() != userId) {
            log.warn("Право редактирования объекта не подтверждено!");
            throw new ObjectNotFoundException("Право редактирования объекта не подтверждено!");
        }
    }

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }
}
