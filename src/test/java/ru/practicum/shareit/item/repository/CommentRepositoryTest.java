package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    CommentRepository commentRepository;
    Item item;
    User user;
    User user2;
    User user3;
    ItemRequest itemRequest;
    ItemRequest itemRequest2;
    Booking booking;
    Booking booking2;
    Comment comment;
    Comment comment2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "user", "user@user.com"));
        user2 = userRepository.save(new User(2L, "user2", "user2@user.com"));
        user3 = userRepository.save(new User(3L, "user3", "user3@user.com"));
        itemRequest = itemRequestRepository.save(new ItemRequest(1L, "item", user2.getId(), LocalDateTime.now()));
        item = itemRepository.save(new Item(1L, "item", "description", true,
                user.getId(), 1L));
        booking = bookingRepository.save(new Booking(1L, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusHours(1),
                1L, 2L, Status.APPROVED));
        booking2 = bookingRepository.save(new Booking(2L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3),
                1L, 3L, Status.APPROVED));
        comment = commentRepository.save(new Comment(1L, "comment", 1L, 2L, LocalDateTime.now().plusMinutes(10)));
        comment2 = commentRepository.save(new Comment(2L, "comment2", 1L, 3L, LocalDateTime.now().plusHours(2)));
    }

    @Test
    void findAllByItem() {
        List<Comment> comments = commentRepository.findAllByItem(item.getId());
        assertEquals(2, comments.size());
        assertEquals("comment2", comments.get(1).getText());
        assertEquals(2, comments.get(0).getAuthor());
    }
}