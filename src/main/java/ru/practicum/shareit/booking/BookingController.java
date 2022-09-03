package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    BookingDtoForReturn addBooking(@Valid @RequestBody BookingDto bookingDto,
                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    BookingDtoForReturn approvedBooking(@Valid @RequestParam (value = "approved") Boolean approved,
                               @PathVariable long bookingId,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDtoForReturn getBooking(@Valid @PathVariable long bookingId,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    List<BookingDtoForReturn> getAllBookingByBooker(@Valid @RequestParam (value = "state", defaultValue = "ALL") String state,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllBookingByBooker(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDtoForReturn> getAllBookingByUser(@Valid @RequestParam (value = "state", defaultValue = "ALL") String state,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getAllBookingByUser(userId, state);
    }
}
