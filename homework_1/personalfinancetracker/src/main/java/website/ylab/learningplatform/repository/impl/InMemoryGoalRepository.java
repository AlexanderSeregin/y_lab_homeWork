package website.ylab.learningplatform.repository.impl;

import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.repository.GoalRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of GoalRepository
 */
public class InMemoryGoalRepository implements GoalRepository {
    private static final InMemoryGoalRepository INSTANCE = new InMemoryGoalRepository();
    private final Map<Long, Goal> repository = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private InMemoryGoalRepository() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get singleton instance
     * @return repository instance
     */
    public static InMemoryGoalRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Goal> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public Goal save(Goal entity) {
        return null;
    }

//    @Override
//    public Goal save(Goal goal) {
//        if (goal.getUserId() == null) {
//            goal.setUserId(idGenerator.incrementAndGet());
//        }
//        //???repository.put(goal.getUserId(), goal);
//        return goal;
//    }

    @Override
    public List<Goal> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public void delete(Goal goal) {
        repository.remove(goal.getUserId());
    }

    @Override
    public void deleteById(Long id) {
        repository.remove(id);
    }

    @Override
    public Optional<Goal> findByUserId(Long userId) {
        return repository.values().stream()
                .filter(goal -> goal.getUserId().equals(userId))
                .findFirst();
    }
}
