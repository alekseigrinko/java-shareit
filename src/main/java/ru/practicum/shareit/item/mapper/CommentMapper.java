package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoForReturnItem;
import ru.practicum.shareit.item.dto.ItemDtoForReturnByBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.dto.UserForReturnByBooker;

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

    public static CommentDto toCommentDto(Comment comment, ItemDtoForReturnByBooking item, UserForReturnByBooker author) {
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
