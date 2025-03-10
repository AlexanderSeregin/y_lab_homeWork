package website.ylab.learningplatform.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import website.ylab.learningplatform.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private UserDao userDao;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDao = UserDao.getInstance();
        // Clear any existing data by using reflection to reset the repository
        try {
            java.lang.reflect.Field field = UserDao.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(userDao, new java.util.HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        testUser = new User("Test User", "test@example.com", "hashedPassword");
    }

    @Test
    void testSaveAndGet() {
        userDao.save(testUser);

        Optional<User> result = userDao.get(testUser.getId());

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    void testDelete() {
        userDao.save(testUser);
        userDao.delete(testUser);

        Optional<User> result = userDao.get(testUser.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdate() {
        userDao.save(testUser);

        String newName = "Updated User";
        testUser.setName(newName);
        userDao.update(testUser);

        Optional<User> result = userDao.get(testUser.getId());

        assertTrue(result.isPresent());
        assertEquals(newName, result.get().getName());
    }

    @Test
    void testGetAll() {
        userDao.save(testUser);

        User anotherUser = new User("Another User", "another@example.com", "anotherPassword");
        userDao.save(anotherUser);

        Iterable<User> users = userDao.getAll();
        List<User> userList = new ArrayList<>();
        users.forEach(userList::add);

        assertEquals(2, userList.size());
        assertTrue(userList.contains(testUser));
        assertTrue(userList.contains(anotherUser));
    }

    @Test
    void testFindByEmail() {
        userDao.save(testUser);

        User result = userDao.findByEmail(testUser.getEmail());

        assertNotNull(result);
        assertEquals(testUser, result);
    }

    @Test
    void testFindByEmail_NotFound() {
        User result = userDao.findByEmail("nonexistent@example.com");

        assertNull(result);
    }
}