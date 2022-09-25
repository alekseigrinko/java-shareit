package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.booking.State.checkState;

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
    BookingResponseDto addBooking(@Valid @RequestBody BookingDto bookingDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    BookingResponseDto approvedBooking(@Valid @RequestParam(value = "approved") Boolean approved,
                                       @PathVariable long bookingId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingResponseDto getBooking(@Valid @PathVariable long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    List<BookingResponseDto> getAllBookingByBooker(@RequestParam(value = "state", defaultValue = "ALL") String line,
                                                   @RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        State state = checkState(line).orElseThrow(() -> new BadRequestException("Unknown state: " + line));
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
        return bookingService.getAllBookingByBooker(userId, state, pageRequest);
    }

    @GetMapping("/owner")
    List<BookingResponseDto> getAllBookingByUser(@RequestParam(value = "state", defaultValue = "ALL") String line,
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                 @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        State state = checkState(line).orElseThrow(() -> new BadRequestException("Unknown state: " + line));
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
        return bookingService.getAllBookingByUser(userId, state, pageRequest);
    }
}
