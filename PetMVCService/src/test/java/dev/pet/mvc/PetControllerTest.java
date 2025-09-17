package dev.pet.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pet.config.TestSpringConfig;
import dev.pet.mvc.model.PetDto;
import dev.pet.mvc.model.UserDto;
import dev.pet.mvc.services.PetService;
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


@AutoConfigureMockMvc
@SpringBootTest
@Import(TestSpringConfig.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSuccessCreatePet() throws Exception {
        createAndReturnTestUser();

        PetDto petDto = PetDto.builder()
                .id(null)
                .name("TestPetName")
                .userId(0L)
                .build();

        String testPetJson = objectMapper.writeValueAsString(petDto);

        String createdTestPetJson = mockMvc.perform(post("/pet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPetJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto PetDtoFromResponse = objectMapper.readValue(createdTestPetJson, PetDto.class);

        Assertions.assertNotNull(PetDtoFromResponse.getId());
        Assertions.assertEquals(petDto.getName(), PetDtoFromResponse.getName());
        Assertions.assertEquals(petDto.getUserId(), PetDtoFromResponse.getUserId());
    }

    @Test
    void shouldSuccessUpdatePet() throws Exception {
        createAndReturnTestUser();

        PetDto petDto = PetDto.builder()
                .id(null)
                .name("TestPetName")
                .userId(0L)
                .build();

        petDto = petService.createPetAndReturn(petDto);

        PetDto petDtoForUpdate = PetDto.builder()
                .id(null)
                .name("NewTestPetName")
                .userId(0L)
                .build();

        String testPetJson = objectMapper.writeValueAsString(petDtoForUpdate);

        String createdTestPetJson = mockMvc.perform(put("/pet/update/{id}", petDto.getId(), petDtoForUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPetJson)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto petDtoFromResponse = objectMapper.readValue(createdTestPetJson, PetDto.class);

        Assertions.assertNotNull(petDtoFromResponse.getId());
        Assertions.assertEquals(petDtoForUpdate.getName(), petDtoFromResponse.getName());
        Assertions.assertEquals(petDtoForUpdate.getUserId(), petDtoFromResponse.getUserId());
    }

    @Test
    void shouldSuccessCreatePetBecauseNotValidParameters_name() throws Exception {
        createAndReturnTestUser();

        PetDto petDto = PetDto.builder()
                .id(null)
                .name(null)
                .userId(0L)
                .build();

        String testPetJson = objectMapper.writeValueAsString(petDto);

        mockMvc.perform(post("/pet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testPetJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    void shouldSuccessSearchPetById() throws Exception {
        createAndReturnTestUser();

        PetDto petDto = PetDto.builder()
                .id(null)
                .name("TestPetName")
                .userId(0L)
                .build();

        petDto = petService.createPetAndReturn(petDto);

        String foundPetDtoFromResponse = mockMvc.perform(get("/pet/{id}", petDto.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto foundPet = objectMapper.readValue(foundPetDtoFromResponse, PetDto.class);

        org.assertj.core.api.Assertions.assertThat(petDto)
                .usingRecursiveComparison()
                .isEqualTo(foundPet);
    }

    @Test
    void shouldSuccessDeletePetById() throws Exception {
        createAndReturnTestUser();

        PetDto petDto = PetDto.builder()
                .id(null)
                .name("TestPetName")
                .userId(0L)
                .build();

        petDto = petService.createPetAndReturn(petDto);
        mockMvc.perform(delete("/pet/delete/{id}", petDto.getId()))
                .andExpect(status().isNoContent());
    }

    private UserDto createAndReturnTestUser() {
        UserDto userDto = UserDto
                .builder()
                .id(null)
                .age(20)
                .email("test@mail.com")
                .name("TestName")
                .pets(null)
                .build();

        return userService.createUserAndReturn(userDto);
    }
}
