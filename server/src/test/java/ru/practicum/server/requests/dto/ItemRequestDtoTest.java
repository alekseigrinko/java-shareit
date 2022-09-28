package ru.practicum.server.requests.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "item", 1L, LocalDateTime.now());


    @Test
    void testSerialize() throws Exception {
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("item");
        assertThat(result).extractingJsonPathNumberValue("$.requester").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequestDto
                .getCreated().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"description\":\"item\",\"requester\":\"1\"}";

        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getDescription()).isNotBlank();
        assertThat(this.json.parseObject(content).getRequester()).isEqualTo(1);
    }
}