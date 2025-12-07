package org.example.repository;

import org.example.model.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(long id);
    List<User> findAll();
    boolean deleteById(long id);
}

