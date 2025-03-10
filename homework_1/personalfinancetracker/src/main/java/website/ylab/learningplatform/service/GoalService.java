package website.ylab.learningplatform.service;

import website.ylab.learningplatform.datasource.GoalDao;
import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.model.User;

import java.math.BigDecimal;

public class GoalService {
    public static Goal getGoalByUserId(long id) {
        return GoalDao.getInstance().get(id).orElse(null);
    }

    public static void setGoal(User user, BigDecimal newGoal) {
        GoalDao.getInstance().save(user.getId(), new Goal(newGoal));
    }
}
