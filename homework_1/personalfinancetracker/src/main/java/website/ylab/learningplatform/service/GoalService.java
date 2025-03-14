package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.impl.PostgresGoalRepository;

import java.math.BigDecimal;

public class GoalService {
    public static Goal getGoalByUserId(long id) {
        return PostgresGoalRepository.getInstance().findByUserId(id).orElse(null);
    }

    public static void setGoal(User user, BigDecimal newGoal) {
        PostgresGoalRepository.getInstance().save(new Goal(newGoal));
    }
}
