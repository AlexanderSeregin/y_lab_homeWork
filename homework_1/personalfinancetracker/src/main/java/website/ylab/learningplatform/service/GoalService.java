package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.GoalRepository;
import website.ylab.learningplatform.repository.impl.PostgresGoalRepository;

import java.math.BigDecimal;

public class GoalService {
    private static final GoalRepository goalRepository = PostgresGoalRepository.getInstance();

    public static Goal getGoalByUserId(long id) {
        return goalRepository.findByUserId(id).orElse(null);
    }

    public static Goal setGoal(User user, BigDecimal newGoal) {
        return goalRepository.save(new Goal(newGoal));
    }

    public static Goal getUserGoal(User user) {
        return goalRepository.findByUserId(user.getId()).orElse(null);
    }

    public static Goal setGoal(Goal goal) {
        return goalRepository.save(goal);
    }
}
