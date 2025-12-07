package org.example.service;

import org.example.exception.UserNotFoundException;
import org.example.model.User;

import java.util.List;

public interface UserService {
    User createUser(String name, String email);
    User getUser(long id) throws UserNotFoundException;
    List<User> getAllUsers();
    User updateUser(long id, String name, String email) throws UserNotFoundException;
    boolean deleteUser(long id);
}
