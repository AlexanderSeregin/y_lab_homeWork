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

    public static void setGoal(User user, BigDecimal newGoal) {
        goalRepository.save(new Goal(newGoal));
    }
}
