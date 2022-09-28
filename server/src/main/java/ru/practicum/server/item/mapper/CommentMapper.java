package ru.practicum.server.item.mapper;

import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.CommentResponseDtoForItem;
import ru.practicum.server.item.model.Comment;

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

    public static CommentDto toCommentDto(Comment comment, BookingResponseDto.ItemResponseDtoForBooking item, BookingResponseDto.UserResponseDtoForBooker author) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                item,
                author.getName(),
                comment.getCreated()

        );
    }

    public static CommentResponseDtoForItem toCommentDtoForReturnItem(Comment comment, String author) {
        return new CommentResponseDtoForItem(
                comment.getId(),
                comment.getText(),
                author,
                comment.getCreated()
        );
    }
}
