package dev.sorokin.eventmanager.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User userDtoToUser(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.login(),
                userDto.age(),
                userDto.password(),
                userDto.role()
        );
    }

    public UserDto userToUserDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.age(),
                user.password(),
                user.role()
        );
    }

    public UserEntity userToUserEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.age(),
                user.password(),
                null
        );
    }

    public User userEntityToUser(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                null,
                UserRole.valueOf(userEntity.getRole())
        );
    }

}
