package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Notification;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NotificationDao implements Dao<Notification> {
    private static final NotificationDao INSTANCE = new NotificationDao();

    public static NotificationDao getInstance() {
        return INSTANCE;
    }

    private final HashMap<Long, List<Notification>> repository = new HashMap<>();
    @Override
    public Optional<Notification> get(long id) {
        if (repository.containsKey(id)) {
            Notification notification = repository.get(id).get(0);
            repository.get(id).remove(0);
            if (repository.get(id).isEmpty()) {
                repository.remove(id);
            }
            return Optional.of(notification);
        }
        return Optional.empty();
    }

    @Override
    public void save(Notification notification) {
        if (!repository.containsKey(notification.getId())) {
            repository.put(notification.getId(), new java.util.LinkedList<>());
        }
        repository.get(notification.getId()).add(notification);
    }
    public void delete(Notification notification) {
        repository.get(notification.getId()).remove(0);
    }

    @Override
    public Iterable<Notification> getAll() {
        return repository.values().stream().flatMap(List::stream).toList();
    }
}
