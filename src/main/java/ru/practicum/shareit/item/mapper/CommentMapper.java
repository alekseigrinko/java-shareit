package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoForReturnItem;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, long authorId) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem().getId(),
                authorId,
                commentDto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment, BookingDtoForReturn.ItemDtoForReturnByBooking item, BookingDtoForReturn.UserForReturnByBooker author) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                item,
                author.getName(),
                comment.getCreated()

        );
    }

    public static CommentDtoForReturnItem toCommentDtoForReturnItem(Comment comment, String author) {
        return new CommentDtoForReturnItem(
                comment.getId(),
                comment.getText(),
                author,
                comment.getCreated()
        );
    }
}
