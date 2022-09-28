package ru.practicum.server.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.item.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    ResponseEntity<Object> createItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@Valid @RequestBody ItemDto itemDto,
                       @PathVariable long itemId,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.updateItem(itemId, userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                      @PathVariable long itemId) {
        return itemClient.deleteItem(itemId, userId);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItem(@PathVariable long itemId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    ResponseEntity<Object> getItemsOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                        @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemClient.getItemsOwner(userId, from, size);
    }

    @GetMapping("/search")
    ResponseEntity<Object> searchItems(@RequestParam(value = "text", required = false) String text,
                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                              @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                             @PathVariable long itemId,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
