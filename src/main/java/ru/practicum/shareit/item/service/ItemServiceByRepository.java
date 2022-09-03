package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserForReturnByBooker;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.mapper.CommentMapper.*;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.UserMapper.toUserDtoForReturnByBooker;

@Slf4j
@Service("ItemServiceByRepository")
public class ItemServiceByRepository implements ItemService {

    final private ItemRepository itemRepository;
    final private UserRepository userRepository;
    final private BookingRepository bookingRepository;
    final private CommentRepository commentRepository;

    public ItemServiceByRepository(ItemRepository itemRepository, UserRepository userRepository,
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
    public ItemDtoWithComment getItem(long itemId) {
        checkItem(itemId);
        log.warn("предоставлена информация по объекту ID: " + itemId);
        List<CommentDtoForReturnItem> commentDtoList = new ArrayList<>();
        for(Comment comment: commentRepository.findAllByItem(itemId)) {
            commentDtoList.add(toCommentDtoForReturnItem(comment, userRepository.findById(comment.getAuthor()).get().getName()));
        }
        return toItemDtoWithComment(itemRepository.findById(itemId).get(), commentDtoList);
    }

    @Override
    public List<ItemDtoForReturn> getItemsOwner(long userId) {
        checkUser(userId);
        List<ItemDtoForReturn> itemList = new ArrayList<>();
        for (Item item: itemRepository.findAllByOwner(userId)) {
            /*ItemDtoForReturn.BookingDtoForReturnItem lastBooking;
            ItemDtoForReturn.BookingDtoForReturnItem nextBooking;
            List<Booking> bookings = findBookingForItem(item.getId(), userId);
            if (bookings == null) {
                itemList.add(toItemDtoForReturn(item, null, null));
            } else
            if (bookings.size() == 1) {
                lastBooking = new ItemDtoForReturn.BookingDtoForReturnItem(bookings.get(0).getId(),
                        bookings.get(0).getBookerId());
                itemList.add(toItemDtoForReturn(item, lastBooking, null));
            } else
            if (bookings.size() > 1) {
                lastBooking = new ItemDtoForReturn.BookingDtoForReturnItem(bookings.get(0).getId(),
                        bookings.get(0).getBookerId());
                nextBooking = new ItemDtoForReturn.BookingDtoForReturnItem(bookings.get(1).getId(),
                        bookings.get(1).getBookerId());
                itemList.add(toItemDtoForReturn(item, lastBooking, nextBooking));}*/
            itemList.add(updateBookingByItem(item, userId));

        }
        log.warn("Выведен список объектов пользователя ID: " + userId);
        return itemList;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            log.debug("Условие поиска не задано");
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text).stream()
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
        UserForReturnByBooker user = toUserDtoForReturnByBooker(userRepository.findById(userId).get());
        UserForReturnByBooker owner = toUserDtoForReturnByBooker(userRepository.findById(item.getOwner()).get());
        commentDto.setItem(toItemDtoForReturnByBooking(itemRepository.findById(itemId).get(), owner));
        commentDto.setAuthor(user);
        commentDto.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(toComment(commentDto)),
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

    private List<Booking> findBookingForItem (long itemId, long userId) {
        if(bookingRepository.findByItemId(itemId) == null) {
            log.debug("Бронирование объекта не найдено");
            return null;
        }
        return bookingRepository.getAllBookingByUserCurrentByItem(itemId, userId, LocalDateTime.now());
    }

    private void checkBookingByUser(long itemId, long userId) {
        checkItem(itemId);
        checkUser(userId);
    }

    private ItemDtoForReturn updateBookingByItem(Item item, long userId) {
        ItemDtoForReturn.BookingDtoForReturnItem lastBooking;
        ItemDtoForReturn.BookingDtoForReturnItem nextBooking;
        List<Booking> bookings = findBookingForItem(item.getId(), userId);
        if (bookings == null) {
            return toItemDtoForReturn(item, null, null);
        } else if (bookings.size() == 1) {
            lastBooking = new ItemDtoForReturn.BookingDtoForReturnItem(bookings.get(0).getId(),
                    bookings.get(0).getBookerId());
            return toItemDtoForReturn(item, lastBooking, null);
        } else if (bookings.size() > 1) {
            lastBooking = new ItemDtoForReturn.BookingDtoForReturnItem(bookings.get(0).getId(),
                    bookings.get(0).getBookerId());
            nextBooking = new ItemDtoForReturn.BookingDtoForReturnItem(bookings.get(1).getId(),
                    bookings.get(1).getBookerId());
            return toItemDtoForReturn(item, lastBooking, nextBooking);
        }
        return toItemDtoForReturn(item, null, null);
    }
}
