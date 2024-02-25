package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.user.UserLoginRequestDto;
import mate.academy.bookstore.dto.user.UserLoginResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
