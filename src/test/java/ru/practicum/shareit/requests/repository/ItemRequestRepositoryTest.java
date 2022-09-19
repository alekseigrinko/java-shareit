package ru.practicum.shareit.requests.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    Item item;
    Item item2;
    User user;
    User user2;
    ItemRequest itemRequest;
    ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "user", "user@user.com"));
        user2 = userRepository.save(new User(2L, "user2", "user2@user.com"));
        itemRequest = itemRequestRepository.save(new ItemRequest(1L, "item", user2.getId(), LocalDateTime.now()));
        itemRequest2 = itemRequestRepository.save(new ItemRequest(2L, "item2", user2.getId(), LocalDateTime.now()));
        item = itemRepository.save(new Item(1L, "item", "description", true,
                user.getId(), 1L));
        item2 = itemRepository.save(new Item(2L, "item2", "description2", true,
                user.getId(), 2L));
    }

    @Test
    void findAllByRequesterOrderByCreatedDesc() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterOrderByCreatedDesc(user2.getId());
        assertEquals(2, itemRequestList.size());
        assertEquals(2, itemRequestList.get(0).getRequester());
    }

    @Test
    void findAllRequests() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("id"));

        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAllRequests(user.getId(), pageRequest);

        assertNotNull(itemRequestPage);
        assertEquals(2, itemRequestPage.getTotalElements());
    }
}