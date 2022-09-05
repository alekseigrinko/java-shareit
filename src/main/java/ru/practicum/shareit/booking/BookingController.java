package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.BadRequestException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
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
    BookingDtoForReturn approvedBooking(@Valid @RequestParam(value = "approved") Boolean approved,
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
    List<BookingDtoForReturn> getAllBookingByBooker(@RequestParam(value = "state", defaultValue = "ALL") String line,
                                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        State state = null;
        try {
            state = State.valueOf(line);
        } catch (IllegalArgumentException e) {
            log.warn("Не корректный параметр поиска");
            throw new BadRequestException("Unknown state: " + line);
        }
        return bookingService.getAllBookingByBooker(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDtoForReturn> getAllBookingByUser(@RequestParam(value = "state", defaultValue = "ALL") String line,
                                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        State state = null;
        try {
            state = State.valueOf(line);
        } catch (IllegalArgumentException e) {
            log.warn("Не корректный параметр поиска");
            throw new BadRequestException("Unknown state: " + line);
        }
        return bookingService.getAllBookingByUser(userId, state);
    }
}
