package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;
    private long bookerId;
    private Status status;
}
