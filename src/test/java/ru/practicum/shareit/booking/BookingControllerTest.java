package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
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
import static ru.practicum.shareit.booking.BookingMapping.toBookingDtoForReturn;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;
import static ru.practicum.shareit.user.UserMapper.toUserDtoForReturnByBooker;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    Item item;
    User user;
    User user2;
    Booking booking;
    BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "user@user.com");
        user2 = new User(2L, "user2", "user2@user.com");
        item = new Item(1L, "item", "description", false, user.getId(), user2.getId());
        booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
                item.getId(), user2.getId(), Status.WAITING);
        bookingResponseDto = toBookingDtoForReturn(booking,
                toItemDtoForReturnByBooking(item, toUserDtoForReturnByBooker(user)),
                toUserDtoForReturnByBooker(user2));
    }

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(any(BookingDto.class), anyLong())).thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));

        verify(bookingService, times(1))
                .addBooking(any(BookingDto.class), anyLong());
    }

    @Test
    void approvedBooking() throws Exception {
        long pathVariable = 1;
        bookingResponseDto.setStatus(Status.APPROVED);
        when(bookingService.approvedBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/" + pathVariable)
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));

        verify(bookingService, times(1))
                .approvedBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getBooking() throws Exception {
        long pathVariable = 1;
        bookingResponseDto.setStatus(Status.APPROVED);
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/" + pathVariable)
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(item.getName())))
                .andExpect(jsonPath("$.item.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));

        verify(bookingService, times(1))
                .getBooking(anyLong(), anyLong());
    }

    @Test
    void getAllBookingByBooker() throws Exception {
        List<BookingResponseDto> bookingTestList = new ArrayList<>();
        bookingResponseDto.setStatus(Status.APPROVED);
        bookingTestList.add(bookingResponseDto);

        when(bookingService.getAllBookingByBooker(anyLong(), any(State.class), any(PageRequest.class))).
                thenReturn(bookingTestList);

        mockMvc.perform(get("/bookings/")
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user2.getId())
                        .param("state", "ALL"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(item.getName())))
                .andExpect(jsonPath("$[0].item.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(Status.APPROVED.toString())));

        verify(bookingService, times(1))
                .getAllBookingByBooker(anyLong(), any(State.class), any(PageRequest.class));
    }

    @Test
    void getAllBookingByUser() throws Exception {
        List<BookingResponseDto> bookingTestList = new ArrayList<>();
        bookingResponseDto.setStatus(Status.APPROVED);
        bookingTestList.add(bookingResponseDto);

        when(bookingService.getAllBookingByUser(anyLong(), any(State.class), any(PageRequest.class))).
                thenReturn(bookingTestList);

        mockMvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user.getId())
                        .param("state", "ALL"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.name", is(item.getName())))
                .andExpect(jsonPath("$[0].item.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(Status.APPROVED.toString())));

        verify(bookingService, times(1))
                .getAllBookingByUser(anyLong(), any(State.class), any(PageRequest.class));

    }
}