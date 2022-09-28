package ru.practicum.server.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.Status;
import ru.practicum.server.booking.dto.BookingResponseDto;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.item.dto.*;
import ru.practicum.server.item.mapper.ItemMapper;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.CommentRepository;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.booking.BookingMapping.toBookingDtoForReturnItem;
import static ru.practicum.server.item.mapper.CommentMapper.*;
import static ru.practicum.server.item.mapper.ItemMapper.*;
import static ru.practicum.server.user.UserMapper.toUserDtoForReturnByBooker;

@Slf4j
@Service
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemServiceImp(ItemRepository itemRepository, UserRepository userRepository,
                          BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        checkUser(userId);
        itemDto.setOwner(userId);
        return toItemDto(itemRepository.save(toItem(itemDto)));
    }

    @Override
    public ItemDto updateItem(long itemId, long userId, ItemDto itemDto) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        Item itemInMemory = itemRepository.findById(itemId).get();
        if (itemDto.getName() != null) {
            itemInMemory.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemInMemory.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemInMemory.setAvailable(itemDto.getAvailable());
        }
        log.debug("Обновлен объект :" + itemInMemory);
        return toItemDto(itemRepository.save(itemInMemory));
    }

    @Override
    public String deleteItem(long itemId, long userId) {
        checkItem(itemId);
        checkItemByUser(itemId, userId);
        itemRepository.deleteById(itemId);
        return "Объект ID: " + itemId + ", удален";
    }

    @Override
    public ItemResponseDtoWithComment getItem(long itemId, long userId) {
        checkItem(itemId);

        log.warn("предоставлена информация по объекту ID: " + itemId);
        List<CommentResponseDtoForItem> commentDtoList = new ArrayList<>();
        for (Comment comment: commentRepository.findAllByItem(itemId)) {
            commentDtoList.add(toCommentDtoForReturnItem(comment, userRepository.findById(comment.getAuthor()).get().getName()));
        }
        Item item = itemRepository.findById(itemId).get();
        return toItemDtoWithComment(updateBookingByItem(item, item.getOwner(), userId), commentDtoList);
    }

    @Override
    public List<ItemResponseDto> getItemsOwner(long userId, PageRequest pageRequest) {
        checkUser(userId);
        List<ItemResponseDto> itemList = new ArrayList<>();
        for (Item item: itemRepository.findAllByOwner(userId,pageRequest)) {
            itemList.add(updateBookingByItem(item, userId, userId));

        }
        log.warn("Выведен список объектов пользователя ID: " + userId);
        return itemList;
    }

    @Override
    public List<ItemDto> searchItems(String text, PageRequest pageRequest) {
        if (text.isBlank()) {
            log.debug("Условие поиска не задано");
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text, pageRequest).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, long userId, long itemId) {
        if (userId == 0) {
            log.warn("Некорректный параметр пользователя");
            throw new BadRequestException("Некорректный параметр пользователя");
        }
        checkBookingByUser(itemId, userId);
        Item item = itemRepository.findById(itemId).get();
        BookingResponseDto.UserResponseDtoForBooker user = toUserDtoForReturnByBooker(userRepository.findById(userId).get());
        BookingResponseDto.UserResponseDtoForBooker owner = toUserDtoForReturnByBooker(userRepository.findById(item.getOwner()).get());
        commentDto.setItem(toItemDtoForReturnByBooking(itemRepository.findById(itemId).get(), owner));
        commentDto.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(toComment(commentDto, userId)),
                toItemDtoForReturnByBooking(itemRepository.findById(itemId).get(), owner), user);
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

    private List<Booking> findBookingForItem(long itemId, long userId) {
        return bookingRepository.getAllBookingByUserCurrentByItem(itemId, userId);
    }

    private void checkBookingByUser(long itemId, long userId) {
        checkItem(itemId);
        checkUser(userId);
        List<Booking> bookingList = bookingRepository.findAllByItemId(itemId);
        if (bookingList.size() == 0) {
            throw new BadRequestException("Объектом еще не пользовались!");
        }
        if (bookingList.get(0).getStatus() == Status.WAITING) {
            throw new BadRequestException("Объектом еще не пользовались!");
        }
        if (bookingList.get(0).getStatus() == Status.REJECTED) {
            throw new BadRequestException("Объектом еще не пользовались!");
        }
    }

    private ItemResponseDto updateBookingByItem(Item item, long userId, long checkId) {
        if (item.getOwner() != checkId) {
            return toItemDtoForReturn(item, null, null);
        }
        List<Booking> bookings = findBookingForItem(item.getId(), userId);
        if (bookings.size() == 0) {
            return toItemDtoForReturn(item, null, null);
        }
        Booking lastBooking = bookingRepository.getLastBooking(item.getId(), userId, LocalDateTime.now());
        Booking nextBooking = bookingRepository.getNextBooking(item.getId(), userId, LocalDateTime.now());
        if (lastBooking == null) {
            return toItemDtoForReturn(item, null, toBookingDtoForReturnItem(nextBooking));
        }
        if (nextBooking == null) {
            return toItemDtoForReturn(item, toBookingDtoForReturnItem(lastBooking), null);
        }
        return toItemDtoForReturn(item, toBookingDtoForReturnItem(lastBooking), toBookingDtoForReturnItem(nextBooking));
    }
}
