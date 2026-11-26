package dev.sorokin.eventmanager.user;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultAdminInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userService.existsByLogin("Admin")) {
            return;
        }
        String password = passwordEncoder.encode("Admin");
        userService.createAdmin(
                new User(
                        null,
                        "Admin",
                        30,
                        password,
                        UserRole.ADMIN
                )
        );
    }
}
