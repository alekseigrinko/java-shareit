package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDtoWithComment;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long itemId, long userId, ItemDto itemDto);

    String deleteItem(long itemId, long userId);

    ItemResponseDtoWithComment getItem(long itemId, long userId);

    List<ItemResponseDto> getItemsOwner(long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(CommentDto commentDto, long userId, long itemId);
}
