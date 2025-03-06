package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.User;

import java.util.HashMap;
import java.util.Optional;

public class UserDao implements Dao<User> {
    static final UserDao INSTANCE = new UserDao();
    public static UserDao getInstance() {
        return INSTANCE;
    }
    private HashMap<Long, User> repository = new HashMap<>();

    @Override
    public void save(User user) {
        repository.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        repository.remove(user.getId());
    }

    @Override
    public void update(User user) {
        repository.put(user.getId(), user);
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Iterable<User> getAll() {
        return repository.values();
    }

    public User findByEmail(String email) {
        return repository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
