package dev.pet.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pet.config.TestSpringConfig;
import dev.pet.mvc.model.PetDto;
import dev.pet.mvc.model.UserDto;
import dev.pet.mvc.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSpringConfig.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSuccessCreateUser() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(20)
                .email("test@mail.com")
                .name("TestName")
                .pets(null)
                .build();

        String testUserJson = objectMapper.writeValueAsString(userDto);

        String createdTestUserJson = mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userDtoFromResponse = objectMapper.readValue(createdTestUserJson, UserDto.class);

        Assertions.assertNotNull(userDtoFromResponse.getId());
        Assertions.assertEquals(userDto.getName(), userDtoFromResponse.getName());
        Assertions.assertEquals(userDto.getAge(), userDtoFromResponse.getAge());
        Assertions.assertEquals(userDto.getEmail(), userDtoFromResponse.getEmail());
    }

    @Test
    void shouldNotCreateUserBecauseNotValidParameters_age() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(500)
                .email("test@mail.com")
                .name("TestName")
                .pets(null)
                .build();

        String testUserJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    void shouldNotCreateUserBecauseNotValidParameters_name() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(5)
                .email("test@mail.com")
                .name(null)
                .pets(null)
                .build();

        String testUserJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    void shouldSuccessUpdateUser() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(20)
                .email("test@mail.com")
                .name("TestName")
                .pets(null)
                .build();

        userDto = userService.createUserAndReturn(userDto);

        UserDto userDtoForUpdate = UserDto
                .builder()
                .id(null)
                .age(20)
                .email("test@mail.com")
                .name("NewTestName")
                .pets(null)
                .build();

        String testUserJson = objectMapper.writeValueAsString(userDtoForUpdate);

        String createdTestUserJson = mockMvc.perform(put("/user/update/{id}", userDto.getId(), userDtoForUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUserJson)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userDtoFromResponse = objectMapper.readValue(createdTestUserJson, UserDto.class);

        Assertions.assertNotNull(userDtoFromResponse.getId());
        Assertions.assertEquals(userDtoForUpdate.getName(), userDtoFromResponse.getName());
        Assertions.assertEquals(userDto.getId(), userDtoFromResponse.getId());
    }


    @Test
    void shouldSuccessSearchUserById() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(20)
                .email("test@mail.com")
                .name("TestName")
                .pets(null)
                .build();

        userDto = userService.createUserAndReturn(userDto);

        String foundUserDtoFromResponse = mockMvc.perform(get("/user/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto foundUser = objectMapper.readValue(foundUserDtoFromResponse, UserDto.class);

        org.assertj.core.api.Assertions.assertThat(userDto)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldSuccessDeleteUserById() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(20)
                .email("test@mail.com")
                .name("TestName")
                .pets(null)
                .build();

        userDto = userService.createUserAndReturn(userDto);

        mockMvc.perform(delete("/user/delete/{id}", userDto.getId()))
                .andExpect(status().isNoContent());
    }

}
