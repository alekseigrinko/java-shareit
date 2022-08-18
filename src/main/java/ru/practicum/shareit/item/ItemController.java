package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @PostMapping
    ItemDto createItem(@RequestHeader("X-Later-User-Id")
            @Validated ({Create.class}) @RequestBody ItemDto itemDto) {
        return itemDto;
    }

    @PutMapping
    ItemDto updateItem(@Validated ({Update.class})@RequestBody ItemDto itemDto) {
        return itemDto;
    }

}
