package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestReturnDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestReturnDto> getAllUserRequests(long userId);

    ItemRequestReturnDto getItemRequestById(long requestId, long userId);

    List<ItemRequestReturnDto> getAllRequests(PageRequest pageRequest, long userId);
}
