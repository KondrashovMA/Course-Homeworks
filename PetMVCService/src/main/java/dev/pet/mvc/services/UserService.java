package dev.pet.mvc.services;

import dev.pet.mvc.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private long idCounter = 0;
    private HashMap<Long, UserDto> usersMap = new HashMap<>();

    public UserDto createUserAndReturn(UserDto userDto){
        long id = idCounter++;
        UserDto createdUserDto = UserDto
                .builder()
                .id(id)
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .pets(userDto.getPets())
                .build();
        createdUserDto.setPets(new ArrayList<>());
        usersMap.put(id, createdUserDto);
        return createdUserDto;
    }

    public UserDto updateUserAndReturn(Long id, UserDto userDto) {
        UserDto userForUpdate = Optional.ofNullable(usersMap.get(id))
                        .orElseThrow(() -> new NoSuchElementException("User with %s don't exists".formatted(id)));
        userForUpdate.setAge(userDto.getAge());
        userForUpdate.setEmail(userDto.getEmail());
        userForUpdate.setName(userDto.getName());
        userForUpdate.setPets(userDto.getPets());
        usersMap.put(id, userForUpdate);
        return userForUpdate;
    }

    public void deleteUserById(Long id) {
        if(!usersMap.containsKey(id)) throw new NoSuchElementException("User with %s don't exists".formatted(id));
        usersMap.remove(id);
    }

    public UserDto getUserById(Long id) {
        return Optional.ofNullable(usersMap.get(id))
                .orElseThrow(() -> new NoSuchElementException(
                        "Not found user with id: %s".formatted(id)
                ));
    }
}
