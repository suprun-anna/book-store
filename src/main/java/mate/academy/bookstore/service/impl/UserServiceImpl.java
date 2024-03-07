package mate.academy.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.RoleName;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.role.RoleRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.ShoppingCartService;
import mate.academy.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final RoleName DEFAULT_ROLE_NAME = RoleName.ROLE_USER;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userDto) {
        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new RegistrationException("Can't register user with email "
                    + userDto.email());
        }
        User user = userMapper.toModel(userDto);
        user.setPassword(passwordEncoder.encode(userDto.password()));
        Set<Role> userRoles = new HashSet<>();
        Role userRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new RegistrationException("Default user role not found"));
        userRoles.add(userRole);
        user.setRoles(userRoles);
        user = userRepository.save(user);
        shoppingCartService.createShoppingCart(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by email " + email));
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id= " + id));
    }
}
