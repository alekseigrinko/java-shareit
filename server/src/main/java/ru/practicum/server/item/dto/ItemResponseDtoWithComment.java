package ru.practicum.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.dto.BookingResponseDtoForItem;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDtoWithComment {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private List<CommentResponseDtoForItem> comments;
    private BookingResponseDtoForItem lastBooking;
    private BookingResponseDtoForItem nextBooking;
}
