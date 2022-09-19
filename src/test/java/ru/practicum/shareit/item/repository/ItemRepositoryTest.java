package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

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
    User user3;
    ItemRequest itemRequest;
    ItemRequest itemRequest2;

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
    }

    @Test
    void findAllByOwner() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("id"));
        Page<Item> itemPage = itemRepository.findAllByOwner(user.getId(), pageRequest);
        System.out.println(itemPage);
        System.out.println(itemPage.stream().collect(Collectors.toList()));
        assertNotNull(itemPage);
        assertEquals(2, itemPage.getTotalElements());
        /*assertEquals("item", itemPage.getContent().get(0).getName());*/
        /*assertEquals(1L, itemPage.getContent().get(0).getId());*/
    }

    @Test
    void searchItem() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("id"));
        Page<Item> itemPage = itemRepository.searchItem("description", pageRequest);
        assertNotNull(itemPage);
        assertEquals(2, itemPage.getTotalElements());
        assertEquals("item", itemPage.getContent().get(0).getName());
        assertEquals(1L, itemPage.getContent().get(0).getId());
    }

    @Test
    void findAllByRequestId() {
        List<Item> itemList = itemRepository.findAllByRequestId(item.getRequestId());
        System.out.println(itemList.get(0).getName());
    }
}