package ru.practicum.server.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.mapper.ItemMapper;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.requests.dto.ItemRequestDto;
import ru.practicum.server.requests.dto.ItemRequestReturnDto;
import ru.practicum.server.requests.service.ItemRequestService;
import ru.practicum.server.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    Item item;
    User user;
    User user2;
    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "user@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        item = new Item(1L, "item", "description", false, user.getId(), null);
        itemRequest = new ItemRequest(1, "request", user2.getId(), LocalDateTime.now());
    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.create(anyLong(), any(ItemRequestDto.class))).thenReturn(ItemRequestMapper.toItemRequestDto(itemRequest));

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequest.getRequester()), Long.class));

        verify(itemRequestService, times(1))
                .create(anyLong(), any(ItemRequestDto.class));
    }

    @Test
    void getAllUserRequests() throws Exception {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(ItemMapper.toItemDto(item));
        List<ItemRequestReturnDto> itemRequestReturnDtoList = new ArrayList<>();
        itemRequestReturnDtoList.add(ItemRequestMapper.toItemRequestReturnDto(itemRequest, itemDtoList));

        when(itemRequestService.getAllUserRequests(anyLong())).thenReturn(itemRequestReturnDtoList);

        mockMvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(itemRequest.getRequester()), Long.class));

        verify(itemRequestService, times(1))
                .getAllUserRequests(anyLong());
    }

    @Test
    void getItemRequestById() throws Exception {
        long pathVariable = 1;
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(ItemMapper.toItemDto(item));
        ItemRequestReturnDto itemRequestReturnDto = ItemRequestMapper.toItemRequestReturnDto(itemRequest, itemDtoList);

        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestReturnDto);

        mockMvc.perform(get("/requests/" + pathVariable)
                        .content(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequest.getRequester()), Long.class));

        verify(itemRequestService, times(1))
                .getItemRequestById(anyLong(), anyLong());
    }

    @Test
    void getAllRequests() throws Exception {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(ItemMapper.toItemDto(item));
        List<ItemRequestReturnDto> itemRequestReturnDtoList = new ArrayList<>();
        itemRequestReturnDtoList.add(ItemRequestMapper.toItemRequestReturnDto(itemRequest, itemDtoList));

        when(itemRequestService.getAllRequests(any(PageRequest.class), anyLong())).thenReturn(itemRequestReturnDtoList);

        mockMvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(ItemRequestMapper.toItemRequestDto(itemRequest)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].requester", is(itemRequest.getRequester()), Long.class));

        verify(itemRequestService, times(1))
                .getAllRequests(any(PageRequest.class), anyLong());
    }
}