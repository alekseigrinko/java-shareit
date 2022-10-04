package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDtoForItem {
    private long id;
    private long bookerId;
    private Status status;
}
