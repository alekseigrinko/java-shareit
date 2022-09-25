package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;
    private ItemDto itemDto = new ItemDto(1L, "item", "description", false, 1L, null);

    @Test
    void testSerialize() throws Exception {
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isNull();
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"name\":\"item\",\"description\":\"description\"}";

        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("item");
        assertThat(this.json.parseObject(content).getDescription()).isNotBlank();
    }
}