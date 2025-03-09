package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TransactionDao implements Dao<Transaction> {
    private static final long counter = 0;
    private static final TransactionDao INSTANCE = new TransactionDao();

    private final HashMap<Long, List<Transaction>> repository = new HashMap<>();

    /**
     * Gets the single instance of TransactionDao.
     * @return the single instance of TransactionDao
     */
    public static TransactionDao getInstance() {
        return INSTANCE;
    }


    /**
     * Retrieves a list of {@link Transaction}s associated with the given user ID.
     *
     * @param userId the ID of the user whose transactions are to be retrieved
     * @return an {@link Optional} containing a list of transactions associated with the given user ID, if any;
     *         otherwise, an empty {@link Optional}
     */
    public Optional<List<Transaction>> get(long userId) {
        if (repository.containsKey(userId)) {
            return Optional.of(repository.get(userId));
        }
        return Optional.empty();
    }


    /**
     * Saves the given transaction to the repository associated with the given user ID.
     * @param transaction the transaction to be saved
     */
    public void save(Transaction transaction) {
        if (!repository.containsKey(transaction.getUserId())) {
            repository.put(transaction.getUserId(), new java.util.ArrayList<>());
        }
        repository.get(transaction.getUserId()).add(transaction);
    }

    /**
     * Retrieves all transactions stored in the repository.
     *
     * @return an {@link Iterable} of all transactions stored in the repository
     */
    @Override
    public Iterable<Transaction> getAll() {
        return repository.values().stream().flatMap(List::stream).toList();
    }

    /**
     * Retrieves all transactions associated with the given user.
     *
     * @param user the user whose transactions are to be retrieved
     * @return a list of all transactions associated with the given user, if any;
     *         otherwise, an empty list
     */
    public List<Transaction> getTransactionsByUser(User user) {
        return repository.get(user.getId());
    }
}
