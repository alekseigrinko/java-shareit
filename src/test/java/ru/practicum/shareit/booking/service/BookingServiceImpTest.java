package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.BookingMapping.toBooking;
import static ru.practicum.shareit.booking.BookingMapping.toBookingDtoForReturn;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDtoForReturnByBooking;
import static ru.practicum.shareit.user.UserMapper.toUserDtoForReturnByBooker;

class BookingServiceImpTest {

    BookingService bookingService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;

    User user;
    User user2;
    Item item;
    BookingDto bookingDto;
    BookingResponseDto bookingResponseDto;

    @BeforeEach
    void forStart() {
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        bookingService = new BookingServiceImp(bookingRepository, itemRepository, userRepository);
        user = new User(1L, "user", "user@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        item = new Item(1L, "item", "description", true, user.getId(), null);
        bookingDto = new BookingDto(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1),
                item.getId(), user2.getId(), Status.WAITING);
        bookingResponseDto = toBookingDtoForReturn(toBooking(bookingDto),
                toItemDtoForReturnByBooking(item, toUserDtoForReturnByBooker(user)),
                toUserDtoForReturnByBooker(user2));
    }


    @Test
    void addBooking() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(toBooking(bookingDto));

        BookingResponseDto bookingResponseTest = bookingService.addBooking(bookingDto, user2.getId());

        assertEquals(1, bookingResponseTest.getId());
        assertEquals(1, bookingResponseTest.getItem().getId());
        assertEquals(2, bookingResponseTest.getBooker().getId());
    }

    @Test
    void approvedBooking() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(toBooking(bookingDto)));
        bookingDto.setStatus(Status.APPROVED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(toBooking(bookingDto));

        BookingResponseDto bookingResponseTest = bookingService.approvedBooking(user.getId(), bookingDto.getId(), true);

        assertEquals(1, bookingResponseTest.getId());
        assertEquals(1, bookingResponseTest.getItem().getId());
        assertEquals(2, bookingResponseTest.getBooker().getId());
        assertEquals(Status.APPROVED, bookingResponseTest.getStatus());
    }

    @Test
    void getBooking() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(toBooking(bookingDto)));

        BookingResponseDto bookingResponseTest = bookingService.getBooking(user.getId(), bookingDto.getId());

        assertEquals(1, bookingResponseTest.getId());
        assertEquals(1, bookingResponseTest.getItem().getId());
        assertEquals(2, bookingResponseTest.getBooker().getId());
    }

    @Test
    void getAllBookingByBooker() {
        PageRequest pageRequest = PageRequest.of(5, 10, Sort.by("id"));
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(toBooking(bookingDto));

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl(bookingList));

        List<BookingResponseDto> bookingDtoTestList = bookingService.getAllBookingByBooker(user2.getId(),
                State.ALL, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());
    }

    @Test
    void getAllBookingByUser() {
        PageRequest pageRequest = PageRequest.of(5, 10, Sort.by("id"));
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(toBooking(bookingDto));

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.getAllBookingByUserId(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl(bookingList));

        List<BookingResponseDto> bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),
                State.ALL, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(1, bookingDtoTestList.get(0).getItem().getOwner().getId());
    }
}