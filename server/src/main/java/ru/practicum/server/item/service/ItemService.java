package ru.practicum.server.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemResponseDto;
import ru.practicum.server.item.dto.ItemResponseDtoWithComment;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long itemId, long userId, ItemDto itemDto);

    ItemDto deleteItem(long itemId, long userId);

    ItemResponseDtoWithComment getItem(long itemId, long userId);

    List<ItemResponseDto> getItemsOwner(long userId, PageRequest pageRequest);

    List<ItemDto> searchItems(String text, PageRequest pageRequest);

    CommentDto addComment(CommentDto commentDto, long userId, long itemId);
}
