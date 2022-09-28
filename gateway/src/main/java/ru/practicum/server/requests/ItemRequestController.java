package ru.practicum.server.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    ItemRequestClient itemRequestClient;

    @PostMapping
    ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    ResponseEntity<Object> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getAllUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(@PathVariable long requestId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getItemRequestById(requestId,userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAllRequests(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getAllRequests(userId, from, size);
    }
}
