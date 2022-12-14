package ru.practicum.server.booking;

import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.booking.dto.BookingResponseDtoForItem;

public class BookingMapping {
    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBookerId(),
                bookingDto.getStatus()
        );
    }

    public static BookingResponseDtoForItem toBookingDtoForReturnItem(Booking booking) {
        return new BookingResponseDtoForItem(
                booking.getId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }

    public static BookingResponseDto toBookingDtoForReturn(Booking booking,
                                                           BookingResponseDto.ItemResponseDtoForBooking itemResponseDtoForBooking,
                                                           BookingResponseDto.UserResponseDtoForBooker userResponseDtoForBooker) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemResponseDtoForBooking,
                userResponseDtoForBooker,
                booking.getStatus()
        );
    }
}
