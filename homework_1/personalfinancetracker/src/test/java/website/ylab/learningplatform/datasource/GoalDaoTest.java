package website.ylab.learningplatform.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import website.ylab.learningplatform.model.Goal;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GoalDaoTest {

    private GoalDao goalDao;
    private Goal testGoal;
    private long testUserId = 1L;

    @BeforeEach
    void setUp() {
        goalDao = GoalDao.getInstance();
        // Clear any existing data by using reflection to reset the repository
        try {
            java.lang.reflect.Field field = GoalDao.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(goalDao, new java.util.HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        testGoal = new Goal(new BigDecimal("5000.00"));
    }

    @Test
    void testSaveAndGet() {
        goalDao.save(testUserId, testGoal);

        Optional<Goal> result = goalDao.get(testUserId);

        assertTrue(result.isPresent());
        assertEquals(testGoal.getAmount(), result.get().getAmount());
    }

    @Test
    void testGet_NotFound() {
        Optional<Goal> result = goalDao.get(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdate() {
        goalDao.save(testUserId, testGoal);

        Goal updatedGoal = new Goal(new BigDecimal("6000.00"));
        goalDao.save(testUserId, updatedGoal);

        Optional<Goal> result = goalDao.get(testUserId);

        assertTrue(result.isPresent());
        assertEquals(updatedGoal.getAmount(), result.get().getAmount());
    }
}