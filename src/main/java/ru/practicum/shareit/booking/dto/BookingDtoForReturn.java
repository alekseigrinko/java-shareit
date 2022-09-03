package ru.practicum.shareit.booking.dto;

import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDtoForReturn;
import ru.practicum.shareit.item.dto.ItemDtoForReturnByBooking;
import ru.practicum.shareit.user.dto.UserForReturnByBooker;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoForReturn {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoForReturnByBooking item;
    private UserForReturnByBooker booker;
    private Status status;
}
