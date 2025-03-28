package website.ylab.learningplatform.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.TransactionService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.TransactionDto;
import website.ylab.learningplatform.web.mapper.TransactionMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for transaction operations
 */
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction management API")
public class TransactionController extends BaseController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, 
                                UserService userService,
                                TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Get all transactions for the current user
     *
     * @param request HTTP request
     * @return list of transactions
     */
    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves all transactions for the current user")
    public ResponseEntity<?> getAllTransactions(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        List<Transaction> transactions = transactionService.getUserTransactions(userId);
        List<TransactionDto> transactionDtos = transactions.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(transactionDtos);
    }

    /**
     * Create a new transaction
     *
     * @param transactionDto transaction information
     * @param request HTTP request
     * @return created transaction
     */
    @PostMapping
    @Operation(summary = "Create transaction", description = "Creates a new transaction for the current user")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody TransactionDto transactionDto,
                                            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        try {
            Transaction transaction = transactionMapper.toEntity(transactionDto);
            transaction.setUserId(userId);
            
            Transaction savedTransaction = transactionService.createTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(transactionMapper.toDto(savedTransaction));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating transaction: " + e.getMessage()));
        }
    }

    /**
     * Get a transaction by ID
     *
     * @param id transaction ID
     * @param request HTTP request
     * @return transaction information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction", description = "Retrieves a transaction by ID")
    public ResponseEntity<?> getTransaction(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Transaction not found"));
        }

        // Security check - users can only view their own transactions
        if (!transaction.getUserId().equals(userId) && !user.getIsAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied"));
        }

        return ResponseEntity.ok(transactionMapper.toDto(transaction));
    }

    /**
     * Delete a transaction
     *
     * @param id transaction ID
     * @param request HTTP request
     * @return empty response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction", description = "Deletes a transaction by ID")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Not authenticated"));
        }

        Long userId = (Long) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Transaction not found"));
        }

        // Security check - users can only delete their own transactions
        if (!transaction.getUserId().equals(userId) && !user.getIsAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Access denied"));
        }

        transactionService.deleteTransaction(transaction);
        return ResponseEntity.noContent().build();
    }
}
