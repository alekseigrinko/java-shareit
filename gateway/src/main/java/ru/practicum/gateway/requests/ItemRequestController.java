package ru.practicum.gateway.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {

    ItemRequestClient itemRequestClient;

    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Create itemRequest with description: {}", itemRequestDto.getDescription());
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    ResponseEntity<Object> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all itemRequests by user ID: {}", userId);
        return itemRequestClient.getAllUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(@PathVariable long requestId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get itemRequests by ID: {}", requestId);
        return itemRequestClient.getItemRequestById(requestId,userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAllRequests(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get all itemRequests for user ID: {}", userId);
        return itemRequestClient.getAllRequests(userId, from, size);
    }
}
