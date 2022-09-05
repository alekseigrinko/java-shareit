package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(BookingDto bookingDto, long userId);

    BookingResponseDto approvedBooking(long userId, long bookingId, boolean approved);

    BookingResponseDto getBooking(long userId, long bookingId);

    List<BookingResponseDto> getAllBookingByBooker(long bookerId, State state);

    List<BookingResponseDto> getAllBookingByUser(long userId, State state);
}
