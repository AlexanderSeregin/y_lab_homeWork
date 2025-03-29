package website.ylab.learningplatform.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.Goal;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.GoalRepository;

import java.math.BigDecimal;

@Service
public class GoalService {

    private static GoalRepository goalRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository) {
        GoalService.goalRepository = goalRepository;
    }

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
