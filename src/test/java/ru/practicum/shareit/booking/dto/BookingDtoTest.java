package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;
    private BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1),
            1L, 1L, Status.WAITING);


    @Test
    void testSerialize() throws Exception {
        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStart().format(DateTimeFormatter.ISO_DATE_TIME));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEnd().format(DateTimeFormatter.ISO_DATE_TIME));
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.WAITING.toString());
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"itemId\":\"1\",\"bookerId\":\"1\"}";

        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getItemId()).isEqualTo(1);
        assertThat(this.json.parseObject(content).getBookerId()).isNotNull();
    }
}