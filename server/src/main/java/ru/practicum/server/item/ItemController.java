package ru.practicum.server.item;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemResponseDto;
import ru.practicum.server.item.dto.ItemResponseDtoWithComment;
import ru.practicum.server.item.service.ItemService;

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
    ItemDto createItem(@RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestBody ItemDto itemDto,
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
                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
        return itemService.getItemsOwner(userId, pageRequest);
    }

    @GetMapping("/search")
    List<ItemDto> searchItems(@RequestParam(value = "text", required = false) String text,
                              @RequestParam(value = "from", defaultValue = "0") int from,
                              @RequestParam(value = "size", defaultValue = "10") int size) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));
        return itemService.searchItems(text, pageRequest);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto createComment(@RequestBody CommentDto commentDto,
                             @PathVariable long itemId,
                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addComment(commentDto, userId, itemId);
    }
}
