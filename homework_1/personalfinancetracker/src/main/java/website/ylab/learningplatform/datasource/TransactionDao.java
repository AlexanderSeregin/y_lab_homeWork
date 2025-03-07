package website.ylab.learningplatform.datasource;

import website.ylab.learningplatform.model.Notification;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TransactionDao implements Dao<Transaction> {
    private static long counter = 0;
    private static final TransactionDao INSTANCE = new TransactionDao();

    private final HashMap<Long, List<Transaction>> repository = new HashMap<>();
    public static TransactionDao getInstance() {
        return INSTANCE;
    }


    public Optional<List<Transaction>> get(long Userid) {
       if (repository.containsKey(Userid)) {
           return Optional.of(repository.get(Userid));
       }
       return Optional.empty();
    }


    public void save(Transaction transaction) {
       if (!repository.containsKey(transaction.getUserId())) {
           repository.put(transaction.getUserId(), new java.util.ArrayList<>());
       }
       repository.get(transaction.getUserId()).add(transaction);
    }

    @Override
    public Iterable<Transaction> getAll() {
        return repository.values().stream().flatMap(List::stream).toList();
    }

    public List<Transaction> getTransactionsByUser(User user) {
        return repository.get(user.getId());
    }
}
