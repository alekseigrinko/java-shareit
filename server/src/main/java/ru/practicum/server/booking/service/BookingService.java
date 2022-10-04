package ru.practicum.server.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.booking.State;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(BookingDto bookingDto, long userId);

    BookingResponseDto approvedBooking(long userId, long bookingId, boolean approved);

    BookingResponseDto getBooking(long userId, long bookingId);

    List<BookingResponseDto> getAllBookingByBooker(long bookerId, State state, PageRequest pageRequest);

    List<BookingResponseDto> getAllBookingByUser(long userId, State state, PageRequest pageRequest);
}
