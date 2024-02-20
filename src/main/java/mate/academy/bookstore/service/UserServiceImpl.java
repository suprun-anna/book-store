package mate.academy.bookstore.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userDto)
            throws RegistrationException {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user with email "
                    + userDto.getEmail());
        }
        User user = userMapper.toModel(userDto);
        System.out.println(user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto).orElseThrow(
                () -> new RuntimeException("Can't find user by email " + email));
    }
}
