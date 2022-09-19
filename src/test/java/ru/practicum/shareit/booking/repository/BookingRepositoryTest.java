package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    BookingRepository bookingRepository;
    Item item;
    Item item2;
    User user;
    User user2;
    User user3;

    ItemRequest itemRequest;
    ItemRequest itemRequest2;
    Booking booking;
    Booking booking2;
    Booking booking3;
    Booking booking4;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "user", "user@user.com"));
        user2 = userRepository.save(new User(2L, "user2", "user2@user.com"));
        user3 = userRepository.save(new User(3L, "user3", "user3@user.com"));
        itemRequest = itemRequestRepository.save(new ItemRequest(1L, "item", user2.getId(), LocalDateTime.now()));
        itemRequest2 = itemRequestRepository.save(new ItemRequest(2L, "item2", user3.getId(), LocalDateTime.now()));
        item = itemRepository.save(new Item(1L, "item", "description", true,
                user.getId(), 1L));
        item2 = itemRepository.save(new Item(2L, "item2", "description2", true,
                user.getId(), 2L));
        booking = bookingRepository.save(new Booking(1L, LocalDateTime.now(), LocalDateTime.now(),
                1L, 2L, Status.APPROVED));
        booking2 = bookingRepository.save(new Booking(2L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                2L, 3L, Status.APPROVED));
        booking3 = bookingRepository.save(new Booking(3L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                2L, 2L, Status.APPROVED));
        booking4 = bookingRepository.save(new Booking(4L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                1L, 3L, Status.APPROVED));
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
    }

    @Test
    void getAllBookingPast() {
    }

    @Test
    void getAllBookingCurrent() {
    }

    @Test
    void getAllBookingFuture() {
    }

    @Test
    void getAllBookingByStatus() {
    }

    @Test
    void getAllBookingByUserPast() {
    }

    @Test
    void getAllBookingByUserCurrent() {
    }

    @Test
    void getAllBookingByUserFuture() {
    }

    @Test
    void getAllBookingByUserByStatus() {
    }

    @Test
    void getAllBookingByUserId() {
    }

    @Test
    void getAllBookingByUserCurrentByItem() {
        List<Booking> bookings = bookingRepository.getAllBookingByUserCurrentByItem(item2.getId(), user.getId());
        assertEquals(2, bookings.size());
        assertEquals(3, bookings.get(1).getId());
    }

    @Test
    void findAllByItemId() {
        List<Booking> bookings = bookingRepository.findAllByItemId(item.getId());
        assertEquals(2, bookings.size());
        assertEquals(1, bookings.get(0).getId());
    }

    @Test
    void getLastBooking() {
        Booking bookingTest = bookingRepository.getLastBooking(1, 1, LocalDateTime.now());
        assertEquals(1, bookingTest.getId());
    }

    @Test
    void getNextBooking() {
        Booking bookingTest = bookingRepository.getNextBooking(2, 1, LocalDateTime.now());
        assertEquals(3, bookingTest.getId());
    }
}