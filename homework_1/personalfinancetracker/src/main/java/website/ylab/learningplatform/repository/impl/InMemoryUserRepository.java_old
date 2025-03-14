package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of UserRepository
 */
public class InMemoryUserRepository implements UserRepository {
    private static final InMemoryUserRepository INSTANCE = new InMemoryUserRepository();
    private final Map<Long, User> repository = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private InMemoryUserRepository() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get singleton instance
     * @return repository instance
     */
    public static InMemoryUserRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        }
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public void delete(User user) {
        repository.remove(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        repository.remove(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
