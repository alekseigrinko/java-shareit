package ru.practicum.shareit.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    final ItemService itemService;

    public ItemController(ItemService itemService) {
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
    ItemResponseDtoWithComment getItem(@PathVariable long itemId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    List<ItemResponseDto> getItemsOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                        @Positive
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
        return itemService.getItemsOwner(userId, pageRequest);
    }

    @GetMapping("/search")
    List<ItemDto> searchItems(@RequestParam(value = "text", required = false) String text,
                              @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                              @Positive
                              @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
        return itemService.searchItems(text, pageRequest);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                             @PathVariable long itemId,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addComment(commentDto, userId, itemId);
    }
}
