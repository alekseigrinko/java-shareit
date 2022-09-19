package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDtoWithComment;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.mapper.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItemDtoForReturnByBooking;
import static ru.practicum.shareit.user.UserMapper.toUserDtoForReturnByBooker;

class ItemServiceImpTest {

    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    User user;
    User user2;
    User user3;

    Item item;
    Item item2;
    ItemRequest itemRequest;
    ItemRequest itemRequest2;
    Booking booking;
    Booking booking2;
    Booking booking3;
    Booking booking4;
    Comment comment;
    Comment comment2;

    @BeforeEach
    void forStart() {
        itemRepository = mock(ItemRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        userRepository = mock(UserRepository.class);
        itemService = new ItemServiceImp(itemRepository, userRepository, bookingRepository, commentRepository);
        user = new User(1L, "user", "user@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        user3 = new User(3L, "user3", "user3@user.com");
        itemRequest = new ItemRequest(1L, "item", user2.getId(), LocalDateTime.now());
        itemRequest2 = new ItemRequest(2L, "item2", user3.getId(), LocalDateTime.now());
        item = new Item(1L, "item", "description", true,
                user.getId(), 1L);
        item2 = new Item(2L, "item2", "description2", true,
                user.getId(), 2L);
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), 1L, 2L, Status.APPROVED);
        booking2 = new Booking(2L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 2L, 3L, Status.APPROVED);
        booking3 = new Booking(3L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                2L, 2L, Status.APPROVED);
        booking4 = new Booking(4L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                1L, 3L, Status.APPROVED);
        comment = new Comment(1L, "comment", 1L, 2L, LocalDateTime.now().plusMinutes(10));
        comment2 = new Comment(2L, "comment2", 1L, 3L, LocalDateTime.now().plusHours(2));
    }

    @Test
    void addItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto itemDtoTest = itemService.addItem(user.getId(), toItemDto(item));

        assertEquals("item", itemDtoTest.getName());
        assertEquals(1, itemDtoTest.getOwner());
    }

    @Test
    void updateItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDto itemDtoUpdate = new ItemDto(null, null, "descriptionUpdate", false,
                null, null);
        item.setDescription(itemDtoUpdate.getDescription());
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto itemDtoTest = itemService.updateItem(item.getId(), user.getId(), toItemDto(item));

        assertEquals("item", itemDtoTest.getName());
        assertEquals("descriptionUpdate", itemDtoTest.getDescription());
        assertEquals(1, itemDtoTest.getOwner());
        assertEquals(1, itemDtoTest.getId());
    }

    @Test
    void deleteItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        String response = itemService.deleteItem(item.getId(), user.getId());

        assertEquals("Объект ID: " + item.getId() + ", удален", response);
    }

    @Test
    void getItem() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user3));
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        comments.add(comment2);
        when(commentRepository.findAllByItem(anyLong())).thenReturn(comments);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking4);
        when(bookingRepository.getAllBookingByUserCurrentByItem(anyLong(),anyLong())).thenReturn(bookings);
        when(bookingRepository.getLastBooking(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(booking);
        when(bookingRepository.getNextBooking(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(booking4);

        ItemResponseDtoWithComment itemResponse = itemService.getItem(item.getId(), user.getId());

        assertEquals("item", itemResponse.getName());
        assertEquals("description", itemResponse.getDescription());
        assertEquals(1, itemResponse.getOwner());
        assertEquals(1, itemResponse.getId());
        assertEquals(2, itemResponse.getComments().size());
        assertEquals(1, itemResponse.getLastBooking().getId());
        assertEquals(4, itemResponse.getNextBooking().getId());
    }

    @Test
    void getItemsOwner() {
        PageRequest pageRequest = PageRequest.of(5, 10, Sort.by("id"));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(booking4);
        when(bookingRepository.getAllBookingByUserCurrentByItem(anyLong(),anyLong())).thenReturn(bookings);
        when(bookingRepository.getLastBooking(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(booking);
        when(bookingRepository.getNextBooking(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(booking4);
        when(itemRepository.findAllByOwner(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl(itemList));

        List<ItemResponseDto> itemResponseDtoList = itemService.getItemsOwner(user.getId(), pageRequest);

        assertEquals(1, itemResponseDtoList.size());
        assertEquals(itemResponseDtoList.get(0).getOwner(), user.getId());
        assertEquals(1, itemResponseDtoList.get(0).getLastBooking().getId());
        assertEquals(4, itemResponseDtoList.get(0).getNextBooking().getId());
    }

    @Test
    void searchItems() {
        PageRequest pageRequest = PageRequest.of(5, 10, Sort.by("id"));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.searchItem(anyString(), any(PageRequest.class))).thenReturn(new PageImpl(itemList));

        List<ItemDto> itemDtoList = itemService.searchItems("item", pageRequest);

        assertEquals(1, itemDtoList.size());
        assertEquals("item", itemDtoList.get(0).getName());
    }

    @Test
    void addComment() {
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                item.getId(), user2.getId(), Status.APPROVED);
        Comment comment = new Comment(1L, "comment", item.getId(), user2.getId(), LocalDateTime.now());
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(bookingRepository.findAllByItemId(anyLong())).thenReturn(bookingList);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDto = toCommentDto(comment,
                toItemDtoForReturnByBooking(item, toUserDtoForReturnByBooker(user)),
                toUserDtoForReturnByBooker(user2)
        );
        CommentDto commentDtoTest = itemService.addComment(commentDto, user2.getId(), item.getId());

        assertEquals("comment", commentDtoTest.getText());
        assertEquals("user2", commentDtoTest.getAuthorName());
        assertEquals(commentDto.getAuthorName(), commentDtoTest.getAuthorName());
        assertEquals(comment.getItem(), commentDtoTest.getItem().getId());
    }
}