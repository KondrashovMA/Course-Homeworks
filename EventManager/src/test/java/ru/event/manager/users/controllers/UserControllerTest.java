package ru.event.manager.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.event.manager.users.models.UserDto;
import ru.event.manager.users.models.UserResponseDto;
import ru.event.manager.users.models.auth.JwtResponseDto;
import ru.event.manager.users.models.auth.UserCredentialsDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(locations = {
        "classpath:application.properties",
        "classpath:application-test.properties"
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${test.user.login}")
    private String testUserLogin;

    @Value("${test.user.password}")
    private String testUserPassword;

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void shouldGetUserById() throws Exception {
        Long userUd = 1L;
        String userJson = mockMvc.perform(get("/users/{id}", userUd)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDto userDto = objectMapper.readValue(userJson, UserResponseDto.class);

        Assertions.assertNotNull(userDto);
        Assertions.assertNotNull(userDto.id());
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        UserDto userDto = new UserDto(
                null,
                testUserLogin,
                testUserPassword,
                25
        );

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDto createdUserDto = objectMapper.readValue(createdUserJson, UserResponseDto.class);

        Assertions.assertNotNull(createdUserDto);
        Assertions.assertNotNull(createdUserDto.id());
    }

    @Test
    public void shouldNotRegisterUser_becauseLoginExists() throws Exception {
        UserDto userDto = new UserDto(
                null,
                testUserLogin,
                testUserPassword,
                25
        );

        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                )
                .andExpect(status().is(500));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void shouldAuthenticateUser() throws Exception {
        UserCredentialsDto userCredentialsDto =
                new UserCredentialsDto(testUserLogin, testUserPassword);

        String userCredentialsDtoJson = objectMapper.writeValueAsString(userCredentialsDto);

        String jwtTokenJson = mockMvc.perform(post("/users/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userCredentialsDtoJson)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtResponseDto jwtResponseDto = objectMapper.readValue(jwtTokenJson, JwtResponseDto.class);

        Assertions.assertNotNull(jwtResponseDto);
        Assertions.assertNotNull(jwtResponseDto.jwtToken());
    }
}
