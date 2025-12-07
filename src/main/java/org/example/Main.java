package org.example;

import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.InMemoryUserRepository;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;

public class Main {
    public static void main(String[] args)  {

        UserRepository repository = new InMemoryUserRepository();
        UserService service = new UserServiceImpl(repository);

        // Create
        User u1 = service.createUser("Alex", "alex@example.com");
        User u2 = service.createUser("Mylex", "hylex@example.com");

        // Read
        try {
            System.out.println(service.getUser(u1.getId()));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            service.getUser(10);        // No user exists -> throws exception
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(service.getAllUsers());

        // Update
        User updated = null;
        try {
            updated = service.updateUser(u2.getId(), "Hylex", "hylex@example.com");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(updated);

        // Delete
        boolean deleted = service.deleteUser(u1.getId());
        System.out.println("Deleted: " + deleted);

        System.out.println(service.getAllUsers());
    }
}