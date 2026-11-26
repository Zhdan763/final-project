package dev.sorokin.eventmanager.user;

import dev.sorokin.eventmanager.location.LocationController;
import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import dev.sorokin.eventmanager.security.jwt.JwtTokenResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtAuthenticationService jwtAuthenticationService;

    public UserController(
            UserService userService,
            UserMapper userMapper,
            JwtAuthenticationService jwtAuthenticationService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }


    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        logger.info("Get request for sign-up: login={}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.userToUserDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest
    ) {
        logger.info("Get request for sign-in: login={}", signInRequest.login());
        var token = jwtAuthenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtTokenResponse(token));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Long userId
    ) {
        logger.info("Get request for get user by id: {}", userId);
        var userById = userService.findUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.userToUserDto(userById));
    }
}
