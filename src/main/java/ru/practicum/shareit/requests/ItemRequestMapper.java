package ru.practicum.shareit.requests;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestReturnDto;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequester(),
                itemRequestDto.getCreated()
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequestReturnDto toItemRequestReturnDto(ItemRequest itemRequest, List<ItemDto> responseList) {
        return new ItemRequestReturnDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester(),
                itemRequest.getCreated(),
                responseList
        );
    }
}
