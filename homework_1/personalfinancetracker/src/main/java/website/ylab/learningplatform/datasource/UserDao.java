package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.User;

import java.util.HashMap;
import java.util.Optional;

public class UserDao implements Dao<User> {
    static final UserDao INSTANCE = new UserDao();

    /**
     * Gets the single instance of UserDao.
     *
     * @return the single instance of UserDao
     */
    public static UserDao getInstance() {
        return INSTANCE;
    }

    private final HashMap<Long, User> repository = new HashMap<>();


    /**
     * Saves a user to the repository.
     *
     * @param user the user to save
     */
    public void save(User user) {
        repository.put(user.getId(), user);
    }


    /**
     * Removes a user from the repository.
     *
     * @param user the user to remove
     */
    public void delete(User user) {
        repository.remove(user.getId());
    }


    /**
     * Updates a user in the repository.
     *
     * @param user the user to update
     */
    public void update(User user) {
        repository.put(user.getId(), user);
    }


    /**
     * Retrieves a user from the repository by ID.
     *
     * @param id the ID of the user to retrieve
     * @return an optional containing the user, or an empty optional if no user is found
     */
    public Optional<User> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return an iterable containing all users in the repository
     */
    @Override
    public Iterable<User> getAll() {
        return repository.values();
    }

    /**
     * Finds a user by email address.
     *
     * @param email the email address to search for
     * @return a user with the given email address, or null if no user is found
     */
    public User findByEmail(String email) {
        return repository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
