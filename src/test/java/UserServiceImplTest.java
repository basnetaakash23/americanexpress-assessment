
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.repository.impl.InMemoryUserRepository;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {

    private UserService service;

    @BeforeEach
    void setUp() {
        UserRepository repository = new InMemoryUserRepository();
        service = new UserServiceImpl(repository);
    }

    // ---------------------------------------------------------
    // CREATE USER
    // ---------------------------------------------------------

    @Test
    void testCreateUser_Success() {
        User user = service.createUser("Aakash", "aakash@example.com");

        assertNotNull(user);
        assertEquals("Aakash", user.getName());
        assertEquals("aakash@example.com", user.getEmail());
        assertTrue(user.getId() > 0);
    }

    // ---------------------------------------------------------
    // GET USER
    // ---------------------------------------------------------

    @Test
    void testGetUser_Success() throws UserNotFoundException {
        User created = service.createUser("Renisha", "renisha@example.com");

        User found = service.getUser(created.getId());

        assertNotNull(found);
        assertEquals("Renisha", found.getName());
        assertEquals("renisha@example.com", found.getEmail());
    }

    @Test
    void testGetUser_NotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            service.getUser(999L);
        });
    }

    // ---------------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------------

    @Test
    void testUpdateUser_Success() throws UserNotFoundException {
        User created = service.createUser("Old Name", "old@example.com");

        User updated = service.updateUser(created.getId(), "New Name", "new@example.com");

        assertEquals("New Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
    }

    @Test
    void testUpdateUser_NotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            service.updateUser(500L, "Test", "test@example.com");
        });
    }

    // ---------------------------------------------------------
    // DELETE USER
    // ---------------------------------------------------------

    @Test
    void testDeleteUser_Success() throws UserNotFoundException {
        User created = service.createUser("ToDelete", "delete@example.com");

        boolean result = service.deleteUser(created.getId());

        assertTrue(result);
        assertThrows(UserNotFoundException.class, () -> service.getUser(created.getId()));
    }

    @Test
    void testDeleteUser_NotFound() {
        assertFalse(service.deleteUser(1000L));
    }

    // ---------------------------------------------------------
    // GET ALL USERS
    // ---------------------------------------------------------

    @Test
    void testGetAllUsers_ReturnsList() {
        service.createUser("User1", "user1@example.com");
        service.createUser("User2", "user2@example.com");

        List<User> users = service.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void testGetAllUsers_EmptyList() {
        List<User> users = service.getAllUsers();
        assertTrue(users.isEmpty());
    }
}

