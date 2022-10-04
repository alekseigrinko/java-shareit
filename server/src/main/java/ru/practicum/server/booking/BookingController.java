package ru.practicum.server.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    BookingResponseDto addBooking(@RequestBody BookingDto bookingDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    BookingResponseDto approvedBooking(@RequestParam(value = "approved") Boolean approved,
                                       @PathVariable long bookingId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingResponseDto getBooking(@PathVariable long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    List<BookingResponseDto> getAllBookingByBooker(@RequestParam(value = "state", defaultValue = "ALL") String line,
                                                   @RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(value = "from", defaultValue = "0") int from,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        State state = State.checkState(line).orElseThrow(() -> new BadRequestException("Unknown state: " + line));
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
        return bookingService.getAllBookingByBooker(userId, state, pageRequest);
    }

    @GetMapping("/owner")
    List<BookingResponseDto> getAllBookingByUser(@RequestParam(value = "state", defaultValue = "ALL") String line,
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(value = "from", defaultValue = "0") int from,
                                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        State state = State.checkState(line).orElseThrow(() -> new BadRequestException("Unknown state: " + line));
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
        return bookingService.getAllBookingByUser(userId, state, pageRequest);
    }
}
