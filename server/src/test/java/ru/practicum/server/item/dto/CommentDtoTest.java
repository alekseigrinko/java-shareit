package ru.practicum.server.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.server.item.mapper.ItemMapper.toItemDtoForReturnByBooking;
import static ru.practicum.server.user.UserMapper.toUserDtoForReturnByBooker;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;
    private User user = new User(1L, "user", "user@user.com");
    private Item item = new Item(1L, "item", "description", true, user.getId(), 1L);
    private CommentDto commentDto = new CommentDto(1L, "comment",
            toItemDtoForReturnByBooking(item, toUserDtoForReturnByBooker(user)),
            "author", LocalDateTime.now().plusMinutes(10));

    @Test
    void testSerialize() throws Exception {
        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("comment");
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("author");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"text\":\"comment\",\"authorName\":\"author\"}";

        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getText()).isEqualTo("comment");
        assertThat(this.json.parseObject(content).getAuthorName()).isEqualTo("author");
    }
}