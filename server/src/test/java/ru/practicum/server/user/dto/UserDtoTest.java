package ru.practicum.server.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;
    private UserDto userDto = new UserDto(1L, "user", "user@user.com");


    @Test
    void testSerialize() throws Exception {
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("user@user.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"name\":\"user\",\"email\":\"user@user.com\"}";

        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("user");
        assertThat(this.json.parseObject(content).getEmail()).isEqualTo("user@user.com");
    }
}