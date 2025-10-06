package ru.event.manager.users.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.event.manager.security.jwt.JwtTokenManager;
import ru.event.manager.users.models.UserModel;
import ru.event.manager.users.models.auth.JwtResponseDto;
import ru.event.manager.users.models.auth.UserCredentialsModel;

import static ru.event.manager.users.models.auth.UserRoles.ADMIN;
import static ru.event.manager.users.models.auth.UserRoles.USER;

@Service
public class AuthenticationService {

    private final String adminLogin;
    private final String defaultUserLogin;

    private final String adminPassword;
    private final String defaultUserPassword;

    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public AuthenticationService(
            @Value("${spring.security.admin.login}") String adminLogin,
            @Value("${spring.security.default.user.login}") String defaultUserLogin,
            @Value("${spring.security.admin.password}") String adminPassword,
            @Value("${spring.security.default.user.password}") String defaultUserPassword,
            JwtTokenManager jwtTokenManager,
            AuthenticationManager authenticationManager,
            UserService userService
    ) {
        this.adminLogin = adminLogin;
        this.defaultUserLogin = defaultUserLogin;
        this.adminPassword = adminPassword;
        this.defaultUserPassword = defaultUserPassword;
        this.jwtTokenManager = jwtTokenManager;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public JwtResponseDto authenticateUser(UserCredentialsModel userCredentialsModel) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userCredentialsModel.login(), userCredentialsModel.password()
                )
        );
        return new JwtResponseDto(jwtTokenManager.generateToken(userCredentialsModel.login()));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultUsers() {
        if(!userService.isUserExistsByLogin(adminLogin)) {
            UserModel adminModel = new UserModel(
                    null,
                    "admin",
                    adminPassword,
                    21,
                    ADMIN
            );
            userService.registerUser(adminModel);
        }

        if(!userService.isUserExistsByLogin(defaultUserLogin)) {
            UserModel defaultUserModel = new UserModel(
                    null,
                    "user",
                    defaultUserPassword,
                    21,
                    USER
            );
            userService.registerUser(defaultUserModel);
        }
    }

}
