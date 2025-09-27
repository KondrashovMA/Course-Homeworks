package ru.event.manager.users.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.event.manager.users.mappers.UserAndUserDtoMapper;
import ru.event.manager.users.mappers.UserAuthAndUserAuthDtoMapper;
import ru.event.manager.users.models.User;
import ru.event.manager.users.models.auth.UserAuthDataDto;
import ru.event.manager.users.models.UserDto;
import ru.event.manager.users.models.auth.UserTokenDto;
import ru.event.manager.users.services.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UserService userService;
    private final UserAndUserDtoMapper userAndUserDtoMapper;
    private final UserAuthAndUserAuthDtoMapper userAuthAndUserAuthDtoMapper;
    public UsersController(UserService userService, UserAndUserDtoMapper userAndUserDtoMapper, UserAuthAndUserAuthDtoMapper userAuthAndUserAuthDtoMapper) {
        this.userService = userService;
        this.userAndUserDtoMapper = userAndUserDtoMapper;
        this.userAuthAndUserAuthDtoMapper = userAuthAndUserAuthDtoMapper;
    }

    @PostMapping
    @Operation(summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя с уникальным именем. Каждый новый пользователь относится ко группе USER")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Create user: %s".formatted(userDto.toString()));

        User cratedUser = userService.createUser(userAndUserDtoMapper.toModel(userDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userAndUserDtoMapper.toDto(cratedUser));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Получить информацию о пользователе по id",
            description = "Получение данных пользователя (доступно только админам)")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long userId) {
        log.info("Request to get user with id: %s".formatted(userId));

        User user = userService.getUserById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userAndUserDtoMapper.toDto(user));
    }

    @PostMapping("/auth/{userId}")
    @Operation(summary = "Авторизация пользователя и получение JWT",
            description = "Доступно без авторизации. Получение JWT токена по данным пользователям. пока не реализовано")
    public ResponseEntity<UserTokenDto> getUserAuth(@RequestBody @Valid UserAuthDataDto userAuthDataDto) {
        log.info("Request for token for user with login: %s".formatted(userAuthDataDto.login()));

        String token = userService.getUserAuth(userAuthAndUserAuthDtoMapper.toModel(userAuthDataDto));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserTokenDto(token));
    }
}
