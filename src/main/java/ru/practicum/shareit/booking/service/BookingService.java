package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;

import java.util.List;

public interface BookingService {
    BookingDtoForReturn addBooking(BookingDto bookingDto, long userId);

    BookingDtoForReturn approvedBooking(long userId, long bookingId, boolean approved);

    BookingDtoForReturn getBooking(long userId, long bookingId);

    List<BookingDtoForReturn> getAllBookingByBooker(long bookerId, State state);

    List<BookingDtoForReturn> getAllBookingByUser(long userId, State state);
}
