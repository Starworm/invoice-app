package io.project.invoiceApp.service;

import io.project.invoiceApp.domain.User;
import io.project.invoiceApp.dto.UserDTO;

/**
 * interface for link to User repository
 */
public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
}
