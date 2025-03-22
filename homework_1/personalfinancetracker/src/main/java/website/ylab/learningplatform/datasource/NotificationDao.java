package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Notification;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NotificationDao implements Dao<Notification> {
    private static final NotificationDao INSTANCE = new NotificationDao();

    /**
     * Returns the singleton instance of the NotificationDao.
     *
     * @return the singleton instance of the NotificationDao
     */
    public static NotificationDao getInstance() {
        return INSTANCE;
    }

    private final HashMap<Long, List<Notification>> repository = new HashMap<>();

    /**
     * Retrieves and removes the first notification associated with the given ID from the repository.
     *
     * @param id the ID of the notification to retrieve
     * @return an Optional containing the notification if present, otherwise an empty Optional
     */
    public Optional<Notification> get(long id) {
        if (repository.containsKey(id)) {
            if (repository.get(id).isEmpty()) {
                return Optional.empty();
            }
            Notification notification = repository.get(id).get(0);
            repository.get(id).remove(0);
            if (repository.get(id).isEmpty()) {
                repository.remove(id);
            }
            return Optional.of(notification);
        }
        return Optional.empty();
    }


    /**
     * Adds the given notification to the repository associated with the given ID.
     * If the repository does not contain the given ID, a new list is created.
     *
     * @param notification the notification to add to the repository
     */
    public void save(Notification notification) {
        if (!repository.containsKey(notification.getId())) {
            repository.put(notification.getId(), new java.util.LinkedList<>());
        }
        repository.get(notification.getId()).add(notification);
    }

    /**
     * Removes the first notification associated with the given notification's ID from the repository.
     *
     * @param notification the notification whose associated first entry is to be removed from the repository
     */
    public void delete(Notification notification) {
        repository.get(notification.getId()).remove(0);
    }

    /**
     * Retrieves all notifications stored in the repository.
     *
     * @return an Iterable containing all notifications
     */
    @Override
    public Iterable<Notification> getAll() {
        return repository.values().stream().flatMap(List::stream).toList();
    }
}
