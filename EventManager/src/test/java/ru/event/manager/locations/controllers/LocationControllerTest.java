package ru.event.manager.locations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.event.manager.locations.models.Location;
import ru.event.manager.locations.models.LocationDto;
import ru.event.manager.locations.services.LocationsService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LocationControllerTest {

    @Autowired
    private LocationsService locationsService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void shouldSuccessCreateLocation() throws Exception {
        LocationDto locationDto = new LocationDto(
                0L,
                "TestLocation",
                "TestAddress",
                1000,
                "Test"
        );

        String locationDtoJson = objectMapper.writeValueAsString(locationDto);

        String createdLocationDtoJson = mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationDtoJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        LocationDto createdLocationDto = objectMapper.readValue(createdLocationDtoJson, LocationDto.class);

        Assertions.assertNotNull(createdLocationDto.id());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void shouldNotCreateLocation_becauseDuplicateName() throws Exception {
        final String locationName = "TestName_duplicate";
        Location location = new Location(
                0L,
                locationName,
                "Test address",
                100,
                "test"
        );
        locationsService.createLocation(location);

        LocationDto locationDto = new LocationDto(
                0L,
                locationName,
                "TestAddress",
                1000,
                "Test"
        );

        String locationDtoJson = objectMapper.writeValueAsString(locationDto);

        mockMvc.perform(post("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationDtoJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void shouldSuccessUpdateLocation() throws Exception {
        Location location = new Location(
                1L,
                "TestLocation",
                "TestAddress",
                100,
                "test"
        );
        locationsService.createLocation(location);

        LocationDto locationDtoForUpdate = new LocationDto(
                1L,
                "TestLocation",
                "New TestAddress",
                1000,
                "Test"
        );

        String locationDtoForUpdateJson = objectMapper.writeValueAsString(locationDtoForUpdate);

        String updatedLocationDtoJson = mockMvc.perform(put("/locations/{id}", locationDtoForUpdate.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationDtoForUpdateJson)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        LocationDto updatedLocationDto = objectMapper.readValue(updatedLocationDtoJson, LocationDto.class);

        Assertions.assertEquals(updatedLocationDto.id(),locationDtoForUpdate.id());
        Assertions.assertEquals(updatedLocationDto.address(),locationDtoForUpdate.address());
        Assertions.assertEquals(updatedLocationDto.capacity(),locationDtoForUpdate.capacity());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void shouldNotUpdateLocation_because_capacityLessThenCurrent() throws Exception {
        Location location = new Location(
                1L,
                "TestLocation",
                "TestAddress",
                100,
                "test"
        );
        locationsService.createLocation(location);

        LocationDto locationDtoForUpdate = new LocationDto(
                1L,
                "TestLocation",
                "New TestAddress",
                10,
                "Test"
        );

        String locationDtoForUpdateJson = objectMapper.writeValueAsString(locationDtoForUpdate);

        mockMvc.perform(put("/locations/{id}", location.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(locationDtoForUpdateJson)
                )
                .andExpect(status().is(500));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void shouldSuccessGetAllLocations() throws Exception {
        String locationsJson = mockMvc.perform(get("/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<LocationDto> locationsDto = objectMapper.readValue(locationsJson, List.class);

        Assertions.assertNotNull(locationsDto);
        Assertions.assertFalse(locationsDto.isEmpty());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void shouldSuccessGetLocationById() throws Exception {
        Long locationId = 1L;
        String locationsJson = mockMvc.perform(get("/locations/{id}", locationId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        LocationDto locationDto = objectMapper.readValue(locationsJson, LocationDto.class);

        Assertions.assertNotNull(locationDto);
        Assertions.assertNotNull(locationDto.id());
    }
}
