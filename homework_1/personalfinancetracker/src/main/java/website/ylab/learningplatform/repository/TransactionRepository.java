package website.ylab.learningplatform.repository;

import website.ylab.learningplatform.model.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entity
 */
public interface TransactionRepository extends Repository<Transaction, Long> {
    /**
     * Find transactions by user ID
     *
     * @param userId user ID
     * @return list of transactions for given user
     */
    Optional<List<Transaction>> findByUserId(Long userId);

    /**
     * Find transactions by user ID for date range
     *
     * @param userId   user ID
     * @param fromDate start date
     * @param toDate   end date
     * @return list of transactions for given user and date range
     */
    List<Transaction> findByUserIdAndDateBetween(Long userId, Date fromDate, Date toDate);
}
