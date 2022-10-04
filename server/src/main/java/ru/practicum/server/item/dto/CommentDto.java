package ru.practicum.server.item.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.dto.BookingResponseDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;
    private String text;
    private BookingResponseDto.ItemResponseDtoForBooking item;
    String authorName;
    LocalDateTime created;
}
