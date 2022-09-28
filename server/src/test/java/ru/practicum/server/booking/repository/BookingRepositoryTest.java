package ru.practicum.server.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.Status;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.requests.ItemRequest;
import ru.practicum.server.requests.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

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
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(2L, pageRequest);
        assertEquals(2, bookings.getContent().size());
        assertEquals(2, bookings.getContent().get(0).getItemId());
        assertEquals(1, bookings.getContent().get(1).getId());
    }

    @Test
    void getAllBookingPast() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingPast(2L, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(1, bookings.getContent().get(0).getId());
    }

    @Test
    void getAllBookingCurrent() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingCurrent(3L, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(2, bookings.getContent().get(0).getItemId());
        assertEquals(2, bookings.getContent().get(0).getId());
    }

    @Test
    void getAllBookingFuture() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingFuture(3L, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(4, bookings.getContent().get(0).getId());
    }

    @Test
    void getAllBookingByStatus() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingByStatus(3L, Status.APPROVED, pageRequest);
        assertEquals(2, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(2, bookings.getContent().get(1).getId());
    }

    @Test
    void getAllBookingByUserPast() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingByUserPast(1L, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(1, bookings.getContent().get(0).getId());
    }

    @Test
    void getAllBookingByUserCurrent() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingByUserCurrent(1L, LocalDateTime.now(), pageRequest);
        assertEquals(1, bookings.getContent().size());
        assertEquals(2, bookings.getContent().get(0).getItemId());
        assertEquals(2, bookings.getContent().get(0).getId());
    }

    @Test
    void getAllBookingByUserFuture() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingByUserFuture(1L, LocalDateTime.now(), pageRequest);
        assertEquals(2, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(3, bookings.getContent().get(1).getId());
    }

    @Test
    void getAllBookingByUserByStatus() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingByUserByStatus(1L, Status.APPROVED, pageRequest);
        assertEquals(4, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(1, bookings.getContent().get(3).getId());
    }

    @Test
    void getAllBookingByUserId() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        Page<Booking> bookings = bookingRepository.getAllBookingByUserId(1L, pageRequest);
        assertEquals(4, bookings.getContent().size());
        assertEquals(1, bookings.getContent().get(0).getItemId());
        assertEquals(1, bookings.getContent().get(3).getId());
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