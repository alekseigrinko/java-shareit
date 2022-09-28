package ru.practicum.server.item.mapper;

import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.booking.dto.BookingResponseDtoForItem;
import ru.practicum.server.item.dto.CommentResponseDtoForItem;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemResponseDto;
import ru.practicum.server.item.dto.ItemResponseDtoWithComment;
import ru.practicum.server.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequestId()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequestId()
        );
    }

    public static ItemResponseDto toItemDtoForReturn(Item item, BookingResponseDtoForItem lastBooking,
                                                     BookingResponseDtoForItem nextBooking) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                lastBooking,
                nextBooking
        );
    }

    public static ItemResponseDtoWithComment toItemDtoWithComment(ItemResponseDto item, List<CommentResponseDtoForItem> comments) {
        return new ItemResponseDtoWithComment(
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

    public static BookingResponseDto.ItemResponseDtoForBooking toItemDtoForReturnByBooking(Item item, BookingResponseDto.UserResponseDtoForBooker userResponseDtoForBooker) {
        return new BookingResponseDto.ItemResponseDtoForBooking(
                item.getId(),
                item.getName(),
                item.getAvailable(),
                userResponseDtoForBooker
        );
    }
}
