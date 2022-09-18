package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.BookingMapping.*;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDtoForReturnByBooking;
import static ru.practicum.shareit.user.UserMapper.toUserDtoForReturnByBooker;

@Service
@Slf4j
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImp(BookingRepository bookingRepository, ItemRepository itemRepository,
                             UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookingResponseDto addBooking(BookingDto bookingDto, long userId) {
        checkUser(userId);
        checkItem(bookingDto.getItemId());
        if (itemRepository.findById(bookingDto.getItemId()).get().getOwner() == userId) {
            log.warn("Бронирование не возможно. Вы являетесь владельцем объекта!");
            throw new ObjectNotFoundException("Бронирование не возможно. Вы являетесь владельцем объекта!");
        }
        if (!itemRepository.findById(bookingDto.getItemId()).get().getAvailable()) {
            log.warn("Объект не доступен для бронирования!");
            throw new BadRequestException("Объект не доступен для бронирования!");
        }
        checkData(bookingDto.getStart(), bookingDto.getEnd());
        bookingDto.setStatus(Status.WAITING);
        bookingDto.setBookerId(userId);
        BookingResponseDto.UserResponseDtoForBooker user = toUserDtoForReturnByBooker(userRepository.findById(itemRepository
                .findById(bookingDto.getItemId()).get().getOwner()).get());
        BookingResponseDto.UserResponseDtoForBooker booker = toUserDtoForReturnByBooker(userRepository.findById(userId).get());
        log.debug("Бронирование создано и ожидает подтверждение");
        return toBookingDtoForReturn(bookingRepository.save(toBooking(bookingDto)),
                toItemDtoForReturnByBooking(itemRepository.findById(bookingDto.getItemId()).get(),user),
                booker);
    }

    @Override
    public BookingResponseDto approvedBooking(long userId, long bookingId, boolean approved) {
        checkBooking(bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();
        checkItemByUser(booking.getItemId(), userId);
        if (approved) {
            if (booking.getStatus() != Status.WAITING) {
                throw new BadRequestException("Статус уже подтвержден!");
            }
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.debug("Статус бронирования изменен");
        BookingResponseDto.UserResponseDtoForBooker user = toUserDtoForReturnByBooker(userRepository.findById(userId).get());
        BookingResponseDto.UserResponseDtoForBooker booker = toUserDtoForReturnByBooker(userRepository.findById(booking.getBookerId()).get());
        return toBookingDtoForReturn(bookingRepository.save(booking),
                toItemDtoForReturnByBooking(itemRepository.findById(booking.getItemId()).get(), user),
                booker);
    }

    @Override
    public BookingResponseDto getBooking(long userId, long bookingId) {
        checkUserForBooking(userId, bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();
        BookingResponseDto.UserResponseDtoForBooker user = toUserDtoForReturnByBooker(userRepository.findById(itemRepository
                .findById(booking.getItemId()).get().getOwner()).get());
        BookingResponseDto.UserResponseDtoForBooker booker = toUserDtoForReturnByBooker(userRepository.findById(booking.getBookerId()).get());
        log.debug("Предоставлена информация по бронированию ID: " + bookingId);
        return toBookingDtoForReturn(booking, toItemDtoForReturnByBooking(itemRepository.findById(booking.getItemId()).get(), user),
                booker);
    }

    @Override

    public List<BookingResponseDto> getAllBookingByBooker(long bookerId, State state, PageRequest pageRequest) {
        checkUser(bookerId);
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId, pageRequest);
                break;
            case CURRENT:
                bookingList = bookingRepository.getAllBookingCurrent(bookerId, LocalDateTime.now(), pageRequest);
                break;
            case PAST:
                bookingList = bookingRepository.getAllBookingPast(bookerId, LocalDateTime.now(), pageRequest);
                break;
            case FUTURE:
                bookingList = bookingRepository.getAllBookingFuture(bookerId, LocalDateTime.now(), pageRequest);
                break;
            case WAITING:
                bookingList = bookingRepository.getAllBookingByStatus(bookerId, Status.WAITING, pageRequest);
                break;
            case REJECTED:
                bookingList = bookingRepository.getAllBookingByStatus(bookerId, Status.REJECTED, pageRequest);
                break;
            default:
                log.warn("Не корректный параметр поиска");
                throw new BadRequestException("Unknown state: " + state);
        }
        log.debug("Список по параметру " + state + " предоставлен");
        return bookingListToDto(bookingList);
    }

    @Override
    public List<BookingResponseDto> getAllBookingByUser(long userId, State state, PageRequest pageRequest) {
        checkUser(userId);
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.getAllBookingByUserId(userId, pageRequest);
                break;
            case CURRENT:
                bookingList = bookingRepository.getAllBookingByUserCurrent(userId, LocalDateTime.now(), pageRequest);
                break;
            case PAST:
                bookingList = bookingRepository.getAllBookingByUserPast(userId, LocalDateTime.now(), pageRequest);
                break;
            case FUTURE:
                bookingList = bookingRepository.getAllBookingByUserFuture(userId, LocalDateTime.now(), pageRequest);
                break;
            case WAITING:
                bookingList = bookingRepository.getAllBookingByUserByStatus(userId, Status.WAITING, pageRequest);
                break;
            case REJECTED:
                bookingList = bookingRepository.getAllBookingByUserByStatus(userId, Status.REJECTED, pageRequest);
                break;
            default:
                log.warn("Не корректный параметр поиска");
                throw new BadRequestException("Unknown state: " + state);
        }
        log.debug("Список по параметру " + state + " предоставлен");
        return bookingListToDto(bookingList);
    }

    private void checkItem(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.warn("Объекта с ID " + itemId + " не найдено!");
            throw new ObjectNotFoundException("Объекта с ID " + itemId + " не найдено!");
        }
    }

    private void checkItemByUser(long itemId, long userId) {
        if (itemRepository.findById(itemId).get().getOwner() != userId) {
            log.warn("Право редактирования объекта не подтверждено!");
            throw new ObjectNotFoundException("Право редактирования объекта не подтверждено!");
        }
    }

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }

    private void checkData(LocalDateTime start, LocalDateTime end) {
        if (!start.isAfter(LocalDateTime.now())) {
            log.warn("Некорректный формат даты!");
            throw new BadRequestException("Некорректный формат даты!");
        }
        if (!end.isAfter(LocalDateTime.now())) {
            log.warn("Некорректный формат даты!");
            throw new BadRequestException("Некорректный формат даты!");
        }
        if (!end.isAfter(start)) {
            log.warn("Некорректный формат даты!");
            throw new BadRequestException("Некорректный формат даты!");
        }
    }

    private void checkBooking(long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            log.warn("Бронирование ID: " + bookingId + ", не найдено!");
            throw new ObjectNotFoundException("Бронирование ID: " + bookingId + ", не найдено!");
        }
    }

    private List<BookingResponseDto> bookingListToDto(List<Booking> bookingList) {
        List<BookingResponseDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingResponseDto.UserResponseDtoForBooker user = toUserDtoForReturnByBooker(userRepository.findById(itemRepository
                    .findById(booking.getItemId()).get().getOwner()).get());
            BookingResponseDto.UserResponseDtoForBooker booker = toUserDtoForReturnByBooker(userRepository.findById(booking.getBookerId()).get());
            bookingDtoList.add(toBookingDtoForReturn(booking, toItemDtoForReturnByBooking(itemRepository.findById(booking.getItemId()).get(),
                    user), booker));
        }
        return bookingDtoList;
    }

    private void checkUserForBooking(long userId, long bookingId) {
        checkUser(userId);
        checkBooking(bookingId);
        Item item = itemRepository.findById(bookingRepository.findById(bookingId).get().getItemId()).get();
        if (item.getOwner() != userId) {
            if (bookingRepository.findById(bookingId).get().getBookerId() != userId) {
                log.warn("У пользователя недостаточно прав для просмотра бронирования!");
                throw new ObjectNotFoundException("У пользователя недостаточно прав для просмотра бронирования!");
            }
        }
    }
}
