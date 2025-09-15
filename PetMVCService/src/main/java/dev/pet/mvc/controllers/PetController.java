package dev.pet.mvc.controllers;

import dev.pet.mvc.model.PetDto;
import dev.pet.mvc.services.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{id}")
    public PetDto getPetById(@PathVariable Long id) {
        return Optional.ofNullable(petService.getPetById(id))
                .orElseThrow(() -> new RuntimeException(
                        "Not found user with id: %s".formatted(id)
                ));
    }
    @PostMapping("/create")
    public ResponseEntity<PetDto> createPet(@RequestBody @Valid PetDto petDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petService.createPetAndReturn(petDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petService.updatePetAndReturn(id, petDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable Long id) {
        petService.deletePetById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
