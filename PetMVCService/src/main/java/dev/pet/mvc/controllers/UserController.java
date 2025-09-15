package dev.pet.mvc.controllers;

import dev.pet.mvc.model.UserDto;
import dev.pet.mvc.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return Optional.ofNullable(userService.getUserById(id))
                .orElseThrow(() -> new RuntimeException(
                        "Not found user with id: %s".formatted(id)
                ));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        UserDto createdUser = userService.createUserAndReturn(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.updateUserAndReturn(id, userDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/ping")
    public ResponseEntity<String> getServerAlive() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Server available");
    }
}
