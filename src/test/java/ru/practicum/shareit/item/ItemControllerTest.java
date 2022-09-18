package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

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
import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.UserMapper.toUserDtoForReturnByBooker;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    ItemDto itemDto;
    User user;
    User user2;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "user@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        itemDto = new ItemDto(1L, "item", "description", false, user.getId(), user2.getId());
    }

    @Test
    void createItem() throws Exception {

        when(itemService.addItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.owner", is(user.getId()), Long.class));

        verify(itemService, times(1))
                .addItem(anyLong(), any(ItemDto.class));
    }

    @Test
    void updateItem() throws Exception {
        ItemDto itemDtoUpdate = new ItemDto(null, null, "descriptionUpdate", false,
                null, null);
        long pathVariable = 1;
        itemDto.setDescription(itemDtoUpdate.getDescription());
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);
        mockMvc.perform(patch("/items/" + pathVariable)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoUpdate.getDescription())))
                .andExpect(jsonPath("$.owner", is(user.getId()), Long.class));

        verify(itemService, times(1))
                .updateItem(anyLong(), anyLong(), any(ItemDto.class));
    }


    @Test
    void deleteItem() throws Exception {
        long pathVariable = 1;
        when(itemService.deleteItem(anyLong(), anyLong())).thenReturn(" ");

        mockMvc.perform(delete("/items/" + pathVariable)
                        .content(mapper.writeValueAsString(" "))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk());

        verify(itemService, times(1))
                .deleteItem(anyLong(), anyLong());
    }

    @Test
    void getItem() throws Exception {
        long pathVariable = 1;
        when(itemService.getItem(anyLong(), anyLong())).thenReturn(toItemDtoWithComment(
                toItemDtoForReturn(
                        toItem(itemDto), null, null),
                new ArrayList<>()));

        mockMvc.perform(get("/items/" + pathVariable)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.owner", is(user.getId()), Long.class));

        verify(itemService, times(1))
                .getItem(anyLong(), anyLong());
    }

    @Test
    void getItemsOwner() throws Exception {
        List<ItemResponseDto> itemResponseDtoList = new ArrayList<>();
        itemResponseDtoList.add(toItemDtoForReturn(toItem(itemDto), null, null));
        when(itemService.getItemsOwner(anyLong(), any(PageRequest.class))).thenReturn(itemResponseDtoList);

        mockMvc.perform(get("/items/")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].owner", is(user.getId()), Long.class));

        verify(itemService, times(1))
                .getItemsOwner(anyLong(), any(PageRequest.class));
    }

    @Test
    void searchItems() throws Exception {
        List<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);
        when(itemService.searchItems(anyString(), any(PageRequest.class))).thenReturn(itemDtoList);

        mockMvc.perform(get("/items/search")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", itemDto.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].owner", is(user.getId()), Long.class));

        verify(itemService, times(1))
                .searchItems(anyString(), any(PageRequest.class));
    }

    @Test
    void createComment() throws Exception {
        long pathVariable = 1;

        CommentDto commentDto = new CommentDto(
                1L, "comment",
                toItemDtoForReturnByBooking(toItem(itemDto), toUserDtoForReturnByBooker(user)),
                user2.getName(), LocalDateTime.now()
        );

        when(itemService.addComment(any(CommentDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post("/items/" + pathVariable + "/comment/")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.item.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.authorName", is(user2.getName())));

        verify(itemService, times(1))
                .addComment(any(CommentDto.class), anyLong(), anyLong());
    }
}