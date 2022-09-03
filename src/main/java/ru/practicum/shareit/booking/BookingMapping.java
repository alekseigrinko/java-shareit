package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.item.dto.ItemDtoForReturnByBooking;
import ru.practicum.shareit.user.dto.UserForReturnByBooker;

public class BookingMapping {
    public static Booking toBooking (BookingDto bookingDto){
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBookerId(),
                bookingDto.getStatus()
        );
    }

    public static BookingDto toBookingDto (Booking booking){
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }

    public static BookingDtoForReturn toBookingDtoForReturn (Booking booking,
                                                             ItemDtoForReturnByBooking itemDtoForReturnByBooking,
                                                             UserForReturnByBooker userForReturnByBooker) {
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
