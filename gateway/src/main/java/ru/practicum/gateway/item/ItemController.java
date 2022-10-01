package ru.practicum.gateway.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.CommentDto;
import ru.practicum.gateway.item.dto.Create;
import ru.practicum.gateway.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Validated
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }


    @PostMapping
    ResponseEntity<Object> createItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create item with name: {}", itemDto.getName());
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@Valid @RequestBody ItemDto itemDto,
                       @PathVariable long itemId,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Update item with ID: {}", itemId);
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                      @PathVariable long itemId) {
        log.info("Delete item with ID: {}", itemId);
        return itemClient.deleteItem(itemId, userId);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItem(@PathVariable long itemId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get item with ID: {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    ResponseEntity<Object> getItemsOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                        @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get all item by owner ID: {}", userId);
        return itemClient.getItemsOwner(userId, from, size);
    }

    @GetMapping("/search")
    ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(value = "text", required = false) String text,
                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                              @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get all item by search: {}", text);
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                             @PathVariable long itemId,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create comment by item with ID: {}", itemId);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
