package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestReturnDto;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.requests.ItemRequestMapper.toItemRequestDto;

class ItemRequestServiceImpTest {

    ItemRequestService itemRequestService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    ItemRequestRepository itemRequestRepository;

    User user;
    User user2;
    Item item;
    ItemRequest itemRequest;

    @BeforeEach
    void forStart() {
        itemRepository = mock(ItemRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        userRepository = mock(UserRepository.class);
        itemRequestService = new ItemRequestServiceImp(itemRequestRepository, userRepository, itemRepository);
        user = new User(1L, "user", "user@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        item = new Item(1L, "item", "description", false, user.getId(), user2.getId());
        itemRequest = new ItemRequest(1, "request", user2.getId(), LocalDateTime.now());
    }

    @Test
    void create() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto itemRequestDtoTest = itemRequestService.create(user2.getId(), toItemRequestDto(itemRequest));

        assertEquals("request", itemRequestDtoTest.getDescription());
        assertEquals(2, itemRequestDtoTest.getRequester());
    }

    @Test
    void getItemRequestById() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(itemList);
        when(itemRequestRepository.findById(anyLong())).thenReturn(itemRequest);

        ItemRequestReturnDto itemRequestDtoTest = itemRequestService.getItemRequestById(itemRequest.getId(), user2.getId());

        assertEquals("request", itemRequestDtoTest.getDescription());
        assertEquals(2, itemRequestDtoTest.getRequester());
        assertEquals(1, itemRequestDtoTest.getItems().get(0).getId());
    }

    @Test
    void getAllUserRequests() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(itemList);
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        when(itemRequestRepository.findAllByRequesterOrderByCreatedDesc(anyLong())).thenReturn(itemRequestList);

        List<ItemRequestReturnDto> itemRequestListTest = itemRequestService.getAllUserRequests(user2.getId());

        assertEquals(1, itemRequestListTest.size());
        assertEquals(2, itemRequestListTest.get(0).getRequester());
        assertEquals(1, itemRequestListTest.get(0).getItems().get(0).getId());
    }

    @Test
    void getAllRequests() {
        User user3 = new User(3L, "user3", "user3@user.com");
        ItemRequest itemRequest2 = new ItemRequest(2, "request2", user3.getId(), LocalDateTime.now());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.existsById(anyLong())).thenReturn(true);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByRequestId(anyLong())).thenReturn(itemList);
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest2);
        when(itemRequestRepository.findAllRequests(anyLong(), any(PageRequest.class))).thenReturn(itemRequestList);

        PageRequest pageRequest = PageRequest.of(5, 10, Sort.by("id"));

        List<ItemRequestReturnDto> itemRequestListTest = itemRequestService.getAllRequests(pageRequest, user2.getId());

        assertEquals(1, itemRequestListTest.size());
        assertEquals(3, itemRequestListTest.get(0).getRequester());
        assertEquals(1, itemRequestListTest.get(0).getItems().get(0).getId());
    }
}