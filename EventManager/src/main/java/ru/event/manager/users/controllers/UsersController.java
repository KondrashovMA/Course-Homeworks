package ru.event.manager.users.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.event.manager.users.mappers.JwtResponseMapper;
import ru.event.manager.users.mappers.UserAndUserResponseDtoMapper;
import ru.event.manager.users.mappers.UserCredentialsModelAndUserCredentialsDtoMapper;
import ru.event.manager.users.models.UserResponseDto;
import ru.event.manager.users.models.auth.JwtResponseDto;
import ru.event.manager.users.mappers.UserAndUserDtoMapper;
import ru.event.manager.users.models.UserModel;
import ru.event.manager.users.models.auth.UserCredentialsDto;
import ru.event.manager.users.models.UserDto;
import ru.event.manager.users.services.AuthenticationService;
import ru.event.manager.users.services.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UserService userService;
    private final UserAndUserDtoMapper userAndUserDtoMapper;
    private final AuthenticationService authenticationService;
    private final UserCredentialsModelAndUserCredentialsDtoMapper userCredentialsModelAndUserCredentialsDtoMapper;
    private final UserAndUserResponseDtoMapper userAndUserResponseDtoMapper;
    private final JwtResponseMapper jwtResponseMapper;

    public UsersController(UserService userService, UserAndUserDtoMapper userAndUserDtoMapper,
                           UserCredentialsModelAndUserCredentialsDtoMapper userCredentialsModelAndUserCredentialsDtoMapper,
                           AuthenticationService authenticationService, UserAndUserResponseDtoMapper userAndUserResponseDtoMapper,
                           JwtResponseMapper jwtResponseMapper) {
        this.userService = userService;
        this.userAndUserDtoMapper = userAndUserDtoMapper;
        this.userCredentialsModelAndUserCredentialsDtoMapper = userCredentialsModelAndUserCredentialsDtoMapper;
        this.authenticationService = authenticationService;
        this.userAndUserResponseDtoMapper = userAndUserResponseDtoMapper;
        this.jwtResponseMapper = jwtResponseMapper;
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid @RequestBody UserCredentialsDto userCredentials) {
        log.info("Authenticate user with login: %s".formatted(userCredentials.login()));

        return ResponseEntity.ok(jwtResponseMapper.toDto(
                authenticationService.authenticateUser(userCredentialsModelAndUserCredentialsDtoMapper
                        .toModel(userCredentials))
        ));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserDto userDto) {
        log.info("Create user: %s".formatted(userDto.toString()));

        UserModel cratedUser = userService.registerUser(userAndUserDtoMapper.toModel(userDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userAndUserResponseDtoMapper.toDto(cratedUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") Long userId) {
        log.info("Request to get user with id: %s".formatted(userId));

        UserModel user = userService.getUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userAndUserResponseDtoMapper.toDto(user));
    }
}
