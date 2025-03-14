package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.repository.TransactionRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TransactionRepository
 */
public class InMemoryTransactionRepository implements TransactionRepository {
    private static final InMemoryTransactionRepository INSTANCE = new InMemoryTransactionRepository();
    private final Map<Long, Transaction> repository = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private InMemoryTransactionRepository() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get singleton instance
     * @return repository instance
     */
    public static InMemoryTransactionRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(idGenerator.incrementAndGet());
        }
        repository.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public void delete(Transaction transaction) {
        repository.remove(transaction.getId());
    }

    @Override
    public void deleteById(Long id) {
        repository.remove(id);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return repository.values().stream()
                .filter(transaction -> transaction.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByUserIdAndDateBetween(Long userId, Date fromDate, Date toDate) {
        return repository.values().stream()
                .filter(transaction -> transaction.getUserId().equals(userId))
                .filter(transaction -> {
                    Date date = transaction.getDate();
                    return (date.after(fromDate) || date.equals(fromDate)) &&
                           (date.before(toDate) || date.equals(toDate));
                })
                .collect(Collectors.toList());
    }
}
