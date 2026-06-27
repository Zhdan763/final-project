package dev.sorokin.eventmanager.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("Login: %s already exists".formatted(signUpRequest.login()));
        }

        var hashedPassword = passwordEncoder.encode(signUpRequest.password());
        var userToSave = new UserEntity(
                null,
                signUpRequest.login(),
                signUpRequest.age(),
                hashedPassword,
                UserRole.USER.name()
        );
        var savedUser = userRepository.save(userToSave);
        return userMapper.userEntityToUser(savedUser);
    }

    public User findByLogin(String loginFromToken) {
        var userEntity = userRepository.findByLogin(loginFromToken)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.userEntityToUser(userEntity);
    }

    public User findUserById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User by id: %d not found.".formatted(userId)));
        return userMapper.userEntityToUser(user);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public void createAdmin(User user) {
        var userEntity = userMapper.userToUserEntity(user);
        userEntity.setRole(UserRole.ADMIN.name());
        userRepository.save(userEntity);
    }
}
