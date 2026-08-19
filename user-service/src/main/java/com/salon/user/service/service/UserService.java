package com.salon.user.service.service;

import com.salon.user.service.exception.UserException;
import com.salon.user.service.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long userId) throws UserException;
    List<User>getAllUsers();
    void deleteUser(Long id) throws UserException;
    User updateUser(Long id,User user) throws UserException;
}
