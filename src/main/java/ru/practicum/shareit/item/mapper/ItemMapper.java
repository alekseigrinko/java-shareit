package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoForReturnItem;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserForReturnByBooker;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner()
        );
    }

    public static ItemDtoForReturn toItemDtoForReturn(Item item, BookingDtoForReturnItem lastBooking,
                                                      BookingDtoForReturnItem nextBooking) {
        return new ItemDtoForReturn(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                lastBooking,
                nextBooking
        );
    }

    public static ItemDtoWithComment toItemDtoWithComment(ItemDtoForReturn item, List<CommentDtoForReturnItem> comments) {
        return new ItemDtoWithComment(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                comments,
                item.getLastBooking(),
                item.getNextBooking()
        );
    }

    public static ItemDtoForReturnByBooking toItemDtoForReturnByBooking(Item item, UserForReturnByBooker userForReturnByBooker) {
        return new ItemDtoForReturnByBooking(
                item.getId(),
                item.getName(),
                item.getAvailable(),
                userForReturnByBooker
        );
    }
}
