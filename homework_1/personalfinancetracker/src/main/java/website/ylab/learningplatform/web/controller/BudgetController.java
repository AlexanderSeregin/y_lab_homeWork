package website.ylab.learningplatform.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.ylab.learningplatform.model.Budget;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.BudgetService;
import website.ylab.learningplatform.service.UserService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/budgets")
@Tag(name = "Budget Controller", description = "Endpoints for managing user budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    @Autowired
    public BudgetController(BudgetService budgetService, UserService userService) {
        this.budgetService = budgetService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get user's budget", description = "Retrieves the current budget for the authenticated user")
    public ResponseEntity<?> getUserBudget(@RequestHeader("X-Auth-Token") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Budget budget = budgetService.getUserBudget(user.getId());
        
        if (budget == null) {
            return ResponseEntity.ok(BigDecimal.ZERO);
        }
        
        return ResponseEntity.ok(budget);
    }

    @PostMapping
    @Operation(summary = "Set user's budget", description = "Sets or updates the budget for the authenticated user")
    public ResponseEntity<?> setBudget(@RequestParam BigDecimal amount, @RequestHeader("X-Auth-Token") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Budget amount cannot be negative");
        }

        Budget budget = budgetService.setBudget(user.getId(), amount);
        
        return ResponseEntity.ok(budget);
    }

    @PutMapping
    @Operation(summary = "Update budget", description = "Updates an existing budget with new amount")
    public ResponseEntity<?> updateBudget(@PathVariable Long id, @RequestParam BigDecimal amount, 
                                       @RequestHeader("X-Auth-Token") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Budget amount cannot be negative");
        }

        Budget existingBudget = budgetService.getUserBudget(user.getId());
        
        if (existingBudget == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to update this budget");
        }
        
        existingBudget.setAmount(amount);
        Budget updatedBudget = budgetService.updateBudget(existingBudget);
        
        return ResponseEntity.ok(updatedBudget);
    }
}
