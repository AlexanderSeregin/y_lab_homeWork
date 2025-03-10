package website.ylab.learningplatform.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import website.ylab.learningplatform.model.Budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BudgetDaoTest {

    private BudgetDao budgetDao;
    private Budget testBudget;
    private final long testUserId = 1L;

    @BeforeEach
    void setUp() {
        budgetDao = BudgetDao.getInstance();
        // Clear any existing data by using reflection to reset the repository
        try {
            java.lang.reflect.Field field = BudgetDao.class.getDeclaredField("repository");
            field.setAccessible(true);
            field.set(budgetDao, new java.util.HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        testBudget = new Budget(userId, new BigDecimal("1000.00"));
    }

    @Test
    void testSaveAndGet() {
        budgetDao.save(testUserId, testBudget);

        Optional<Budget> result = budgetDao.get(testUserId);

        assertTrue(result.isPresent());
        assertEquals(testBudget.getAmount(), result.get().getAmount());
    }

    @Test
    void testGetAll() {
        budgetDao.save(testUserId, testBudget);

        Budget anotherBudget = new Budget(userId, new BigDecimal("500.00"));
        long anotherUserId = 2L;
        budgetDao.save(anotherUserId, anotherBudget);

        Iterable<Budget> budgets = budgetDao.getAll();
        List<Budget> budgetList = new ArrayList<>();
        budgets.forEach(budgetList::add);

        assertEquals(2, budgetList.size());

        // Find the budgets by their amounts since we don't have direct equals method
        boolean foundTestBudget = false;
        boolean foundAnotherBudget = false;

        for (Budget budget : budgetList) {
            if (budget.getAmount().equals(testBudget.getAmount())) {
                foundTestBudget = true;
            } else if (budget.getAmount().equals(anotherBudget.getAmount())) {
                foundAnotherBudget = true;
            }
        }

        assertTrue(foundTestBudget);
        assertTrue(foundAnotherBudget);
    }

    @Test
    void testGet_NotFound() {
        Optional<Budget> result = budgetDao.get(999L);

        assertFalse(result.isPresent());
    }
}