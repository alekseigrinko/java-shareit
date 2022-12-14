package ru.practicum.server.requests.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.requests.ItemRequest;
import ru.practicum.server.requests.ItemRequestMapper;
import ru.practicum.server.requests.dto.ItemRequestDto;
import ru.practicum.server.requests.dto.ItemRequestReturnDto;
import ru.practicum.server.requests.repository.ItemRequestRepository;
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
        item = new Item(1L, "item", "description", false, user.getId(), null);
        itemRequest = new ItemRequest(1, "request", user2.getId(), LocalDateTime.now());
    }

    @Test
    void create() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto itemRequestDtoTest = itemRequestService.create(user2.getId(), ItemRequestMapper.toItemRequestDto(itemRequest));

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
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));

        ItemRequestReturnDto itemRequestDtoTest = itemRequestService.getItemRequestById(itemRequest.getId(), user2.getId());

        assertEquals("request", itemRequestDtoTest.getDescription());
        assertEquals(2, itemRequestDtoTest.getRequester());
        Assertions.assertEquals(1, itemRequestDtoTest.getItems().get(0).getId());
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
        Assertions.assertEquals(1, itemRequestListTest.get(0).getItems().get(0).getId());
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
        when(itemRequestRepository.findAllRequests(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl(itemRequestList));

        PageRequest pageRequest = PageRequest.of(5, 10, Sort.by("id"));

        List<ItemRequestReturnDto> itemRequestListTest = itemRequestService.getAllRequests(pageRequest, user2.getId());

        assertEquals(1, itemRequestListTest.size());
        assertEquals(3, itemRequestListTest.get(0).getRequester());
        Assertions.assertEquals(1, itemRequestListTest.get(0).getItems().get(0).getId());
    }

    @Test
    void checkUserTest() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            ItemRequestDto itemRequestDtoTest = itemRequestService.create(5L, ItemRequestMapper.toItemRequestDto(itemRequest));
        });

        assertEquals("???????????????????????? ID: " + 5 + ", ???? ????????????!", thrown.getMessage());
    }

    @Test
    void checkItemRequest() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, " ", user2.getId(), LocalDateTime.now());
        final BadRequestException thrown = assertThrows(BadRequestException.class, () -> {
            ItemRequestDto itemRequestDtoTest = itemRequestService.create(3L, itemRequestDto);
        });

        assertEquals("???????????????? ???? ?????????? ???????? ????????????", thrown.getMessage());
    }

    @Test
    void checkRequest() {
        when(itemRequestRepository.existsById(anyLong())).thenReturn(false);

        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            ItemRequestReturnDto itemRequestDtoTest = itemRequestService.getItemRequestById(3L, 3L);
        });

        assertEquals("???????????? ID: " + 3 + ", ???? ????????????!", thrown.getMessage());
    }
}