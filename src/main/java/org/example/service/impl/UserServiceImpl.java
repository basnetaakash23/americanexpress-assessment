package org.example.service.impl;


import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(String name, String email) {
        long id = idGenerator.getAndIncrement();
        User user = new User(id, name, email);
        return repository.save(user);
    }

    @Override
    public User getUser(long id) throws UserNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User updateUser(long id, String name, String email) throws UserNotFoundException {
        User existing = getUser(id);
        existing.setName(name);
        existing.setEmail(email);
        return repository.save(existing);
    }

    @Override
    public boolean deleteUser(long id) {
        return repository.deleteById(id);
    }
}
