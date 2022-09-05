package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.dto.BookingDtoForReturnItem;

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

    public static BookingDtoForReturnItem toBookingDtoForReturnItem(Booking booking) {
        return new BookingDtoForReturnItem(
                booking.getId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }

    public static BookingDtoForReturn toBookingDtoForReturn(Booking booking,
                                                            BookingDtoForReturn.ItemDtoForReturnByBooking itemDtoForReturnByBooking,
                                                            BookingDtoForReturn.UserForReturnByBooker userForReturnByBooker) {
        return new BookingDtoForReturn(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDtoForReturnByBooking,
                userForReturnByBooker,
                booking.getStatus()
        );
    }
}
