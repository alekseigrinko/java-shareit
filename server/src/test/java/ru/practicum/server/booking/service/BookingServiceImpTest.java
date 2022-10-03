package ru.practicum.server.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.State;
import ru.practicum.server.booking.Status;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.server.booking.BookingMapping.toBooking;
import static ru.practicum.server.booking.BookingMapping.toBookingDtoForReturn;
import static ru.practicum.server.item.mapper.ItemMapper.toItemDtoForReturnByBooking;
import static ru.practicum.server.user.UserMapper.toUserDtoForReturnByBooker;

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
        item = new Item(1L, "item", "description", true, user.getId(), 1L);
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(),
                1L, 2L, Status.APPROVED);
        Booking booking2 = new Booking(2L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),
                1L, 2L, Status.APPROVED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                1L, 2L, Status.APPROVED);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        bookingList.add(booking2);
        bookingList.add(booking3);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking2));
        when(bookingRepository.findById(3L)).thenReturn(Optional.of(booking3));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl(bookingList));

        List<BookingResponseDto> bookingDtoTestList = bookingService.getAllBookingByBooker(user.getId(),
                State.ALL, pageRequest);

        assertEquals(3, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(1, bookingDtoTestList.get(0).getItem().getOwner().getId());

        bookingList.clear();
        bookingList.add(booking2);

        when(bookingRepository.getAllBookingCurrent(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByBooker(user.getId(),State.CURRENT, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(2, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        bookingList.add(booking);

        when(bookingRepository.getAllBookingPast(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByBooker(user.getId(),State.PAST, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        bookingList.add(booking3);

        when(bookingRepository.getAllBookingFuture(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByBooker(user.getId(),State.FUTURE, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(3, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        booking3.setStatus(Status.WAITING);
        bookingList.add(booking3);

        when(bookingRepository.getAllBookingByStatus(anyLong(), any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByBooker(user.getId(),State.WAITING, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(3, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        booking.setStatus(Status.REJECTED);
        bookingList.add(booking);

        when(bookingRepository.getAllBookingByStatus(anyLong(), any(String.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByBooker(user.getId(),State.REJECTED, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());
    }

    @Test
    void getAllBookingByUser() {
        User user3 = new User(3L, "user3", "user3@user.com");
        item = new Item(1L, "item", "description", true, user.getId(), 1L);
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(),
                1L, 2L, Status.APPROVED);
        Booking booking2 = new Booking(2L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),
                1L, 2L, Status.APPROVED);
        Booking booking3 = new Booking(3L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                1L, 3L, Status.APPROVED);

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        bookingList.add(booking2);
        bookingList.add(booking3);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user3));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking2));
        when(bookingRepository.findById(3L)).thenReturn(Optional.of(booking3));
        when(bookingRepository.getAllBookingByUserId(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl(bookingList));

        List<BookingResponseDto> bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),
                State.ALL, pageRequest);

        assertEquals(3, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(1, bookingDtoTestList.get(0).getItem().getOwner().getId());

        bookingList.clear();
        bookingList.add(booking2);

        when(bookingRepository.getAllBookingByUserCurrent(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),State.CURRENT, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(2, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        bookingList.add(booking);

        when(bookingRepository.getAllBookingByUserPast(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),State.PAST, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        bookingList.add(booking3);

        when(bookingRepository.getAllBookingByUserFuture(anyLong(), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),State.FUTURE, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(3, bookingDtoTestList.get(0).getId());
        assertEquals(3, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        booking3.setStatus(Status.WAITING);
        bookingList.add(booking3);

        when(bookingRepository.getAllBookingByUserByStatus(anyLong(), any(Status.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),State.WAITING, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(3, bookingDtoTestList.get(0).getId());
        assertEquals(3, bookingDtoTestList.get(0).getBooker().getId());

        bookingList.clear();
        booking.setStatus(Status.REJECTED);
        bookingList.add(booking);

        when(bookingRepository.getAllBookingByUserByStatus(anyLong(), any(Status.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(bookingList));

        bookingDtoTestList = bookingService.getAllBookingByUser(user.getId(),State.REJECTED, pageRequest);

        assertEquals(1, bookingDtoTestList.size());
        assertEquals(1, bookingDtoTestList.get(0).getId());
        assertEquals(2, bookingDtoTestList.get(0).getBooker().getId());
    }

    @Test
    void checkUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            BookingResponseDto bookingResponseDtoTest = bookingService.addBooking(bookingDto, 5L);
        });

        assertEquals("Пользователь ID: " + 5 + ", не найден!", thrown.getMessage());
    }

    @Test
    void checkBookingTest() {
        when(bookingRepository.existsById(anyLong())).thenReturn(false);

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            BookingResponseDto bookingResponseDtoTest = bookingService.approvedBooking(1L, 7L, true);
        });

        assertEquals("Бронирование ID: " + 7 + ", не найдено!", thrown.getMessage());
    }

    @Test
    void checkItemTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        bookingDto.setItemId(8);
        when(itemRepository.existsById(anyLong())).thenReturn(false);

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            BookingResponseDto bookingResponseDtoTest = bookingService.addBooking(bookingDto, 2L);
        });

        assertEquals("Объекта с ID " + 8 + " не найдено!", thrown.getMessage());
    }

    @Test
    void checkItemByUserTest() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(toBooking(bookingDto)));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            BookingResponseDto bookingResponseDtoTest = bookingService.approvedBooking(3L, 1L, true);
        });

        assertEquals("Право редактирования объекта не подтверждено!", thrown.getMessage());
    }

    @Test
    void checkDataTest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        BookingDto bookingDtoTest = new BookingDto(1L, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1),
                item.getId(), user2.getId(), Status.WAITING);
        when(bookingRepository.save(any(Booking.class))).thenReturn(toBooking(bookingDtoTest));

        final BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            BookingResponseDto bookingResponseDtoTest = bookingService.addBooking(bookingDtoTest, 2L);
        });

        assertEquals("Некорректный формат даты!", thrown.getMessage());

        bookingDtoTest.setStart(LocalDateTime.now().plusMinutes(1));
        bookingDtoTest.setEnd(LocalDateTime.now().minusMinutes(1));

        final BadRequestException thrown2 = assertThrows(BadRequestException.class, () -> {
            BookingResponseDto bookingResponseDtoTest2 = bookingService.addBooking(bookingDtoTest, 2L);
        });

        assertEquals("Некорректный формат даты!", thrown2.getMessage());

        bookingDtoTest.setStart(LocalDateTime.now().plusMinutes(10));
        bookingDtoTest.setEnd(LocalDateTime.now().plusMinutes(9));

        final BadRequestException thrown3 = assertThrows(BadRequestException.class, () -> {
            BookingResponseDto bookingResponseDtoTest3 = bookingService.addBooking(bookingDtoTest, 2L);
        });

        assertEquals("Некорректный формат даты!", thrown3.getMessage());
    }

    @Test
    void checkUserForBookingTest() {
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(toBooking(bookingDto)));

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            BookingResponseDto bookingResponseDtoTest = bookingService.getBooking(3, 1L);
        });

        assertEquals("У пользователя недостаточно прав для просмотра бронирования!", thrown.getMessage());
    }
}