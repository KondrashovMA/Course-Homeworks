package dev.pet.mvc.services;

import dev.pet.mvc.model.PetDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PetService {

    private long idCounter = 0;
    private HashMap<Long, PetDto> petsMap = new HashMap<>();

    private final UserService userService;

    public PetService(UserService userService) {
        this.userService = userService;
    }

    public PetDto createPetAndReturn(PetDto petDto){
        long id = idCounter++;
        PetDto createdPetDto = PetDto
                .builder()
                .id(id)
                .name(petDto.getName())
                .userId(petDto.getUserId())
                .build();

        userService.getUserById(petDto.getUserId()).getPets().add(createdPetDto);

        petsMap.put(id, createdPetDto);
        return createdPetDto;
    }

    public PetDto updatePetAndReturn(Long id, PetDto petDto) {
        PetDto petForUpdate = Optional.ofNullable(petsMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("Pet with %s don't exists".formatted(id)));
        petForUpdate.setName(petDto.getName());

        if(!petForUpdate.getUserId().equals(petDto.getUserId())) {
            userService.getUserById(petForUpdate.getUserId()).getPets().remove(petDto); // delete Pet from old user

            petForUpdate.setUserId(petDto.getUserId());
            userService.getUserById(petDto.getUserId()).getPets().add(petDto); // add Pet to new user
        }

        return petForUpdate;
    }

    public void deletePetById(Long id) {
        if(!petsMap.containsKey(id)) throw new NoSuchElementException("Pet with %s don't exists".formatted(id));
        PetDto deletedPet = petsMap.get(id);
        userService.getUserById(deletedPet.getUserId()).getPets().remove(deletedPet);
        petsMap.remove(id);
    }

    public PetDto getPetById(Long id) {
        return Optional.ofNullable(petsMap.get(id))
                .orElseThrow(() -> new NoSuchElementException(
                        "Not found user with id: %s".formatted(id)
                ));
    }
}
