package ru.practicum.server.requests;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.requests.service.ItemRequestService;
import ru.practicum.server.requests.dto.ItemRequestDto;
import ru.practicum.server.requests.dto.ItemRequestReturnDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping
    List<ItemRequestReturnDto> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getAllUserRequests(userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestReturnDto getItemRequestById(@PathVariable long requestId,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequestById(requestId,userId);
    }

    @GetMapping("/all")
    List<ItemRequestReturnDto> getAllRequests(@RequestParam(value = "from", defaultValue = "0") int from,
                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                              @RequestHeader("X-Sharer-User-Id") long userId) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("created").descending());
        return itemRequestService.getAllRequests(pageRequest, userId);
    }
}
