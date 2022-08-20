package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    ItemService itemService;

    public ItemController(@Qualifier("ItemServiceImp") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    ItemDto createItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@Valid @RequestBody ItemDto itemDto,
                       @PathVariable long itemId,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(itemId, userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    String deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId) {
        return itemService.deleteItem(itemId, userId);
    }


    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    List<ItemDto> getItemsOwner(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsOwner(userId);
    }

    @GetMapping("/search")
    List<ItemDto> searchItems(@RequestParam(value = "text", required = false) String text) {
        return itemService.searchItems(text);
    }
}
