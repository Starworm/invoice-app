package io.project.invoiceApp.service.implementation;

import io.project.invoiceApp.domain.User;
import io.project.invoiceApp.dto.UserDTO;
import io.project.invoiceApp.dtoMapper.UserDTOMapper;
import io.project.invoiceApp.repositories.UserRepository;
import io.project.invoiceApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * implements of UserService interface
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }
}
