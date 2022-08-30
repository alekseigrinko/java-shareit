package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;

@Service
public class BookingServiceByRepository implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceByRepository(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


}
