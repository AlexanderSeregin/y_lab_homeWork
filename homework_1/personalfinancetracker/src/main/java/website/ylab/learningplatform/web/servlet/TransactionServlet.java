package website.ylab.learningplatform.web.servlet;

import website.ylab.learningplatform.model.Transaction;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.TransactionService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.web.dto.TransactionDto;
import website.ylab.learningplatform.web.mapper.TransactionMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionServlet extends BaseServlet {
    private final TransactionService transactionService = new TransactionService();
    private final UserService userService = new UserService();
    private final Validator validator;

    public TransactionServlet() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all transactions for user
            Iterable<Transaction> transactions = TransactionService.getUserTransactions(user);
            List<TransactionDto> transactionDtos = new ArrayList<>();
            
            for (Transaction transaction : transactions) {
                transactionDtos.add(TransactionMapper.INSTANCE.toDto(transaction));
            }
            
            writeResponse(response, transactionDtos);
        } else {
            try {
                // Get transaction by ID
                long transactionId = Long.parseLong(pathInfo.substring(1));
                // TODO: Add method to get transaction by ID
                // For now, we'll get all transactions and filter
                Iterable<Transaction> transactions = TransactionService.getUserTransactions(user);
                Transaction found = null;
                
                for (Transaction transaction : transactions) {
                    if (transaction.getId() == transactionId) {
                        found = transaction;
                        break;
                    }
                }
                
                if (found != null) {
                    writeResponse(response, TransactionMapper.INSTANCE.toDto(found));
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Transaction not found");
                }
            } catch (NumberFormatException e) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid transaction ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        try {
            TransactionDto transactionDto = readRequestBody(request, TransactionDto.class);
            
            // Validate transaction input
            Set<ConstraintViolation<TransactionDto>> violations = validator.validate(transactionDto);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorMessage);
                return;
            }
            
            // Set the user ID
            transactionDto.setUserId(user.getId());
            
            // Create transaction
            try {
                TransactionService.newTransaction(
                        user,
                        transactionDto.isIncome(),
                        transactionDto.getAmount(),
                        transactionDto.getCategory(),
                        transactionDto.getDate() != null ? transactionDto.getDate() : new Date(),
                        transactionDto.getDescription()
                );
                
                // Refresh user to get updated balance
                user = userService.getUserById(user.getId());
                
                // Get the created transaction (latest one)
                Iterable<Transaction> transactions = TransactionService.getUserTransactions(user);
                Transaction latest = null;
                for (Transaction t : transactions) {
                    if (latest == null || t.getId() > latest.getId()) {
                        latest = t;
                    }
                }
                
                if (latest != null) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    writeResponse(response, TransactionMapper.INSTANCE.toDto(latest));
                } else {
                    writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Transaction created but could not be retrieved");
                }
            } catch (IllegalArgumentException e) {
                writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating transaction: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Transaction ID is required");
            return;
        }
        
        try {
            long transactionId = Long.parseLong(pathInfo.substring(1));
            TransactionDto transactionDto = readRequestBody(request, TransactionDto.class);
            
            // Check if transaction exists and belongs to user
            Iterable<Transaction> transactions = TransactionService.getUserTransactions(user);
            boolean found = false;
            
            for (Transaction transaction : transactions) {
                if (transaction.getId() == transactionId) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Transaction not found or does not belong to user");
                return;
            }
            
            // Update transaction fields
            if (transactionDto.getDescription() != null && !transactionDto.getDescription().isEmpty()) {
                TransactionService.changeDescription(user, transactionId, transactionDto.getDescription());
            }
            
            if (transactionDto.getAmount() != null && transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                TransactionService.changeAmount(user, transactionId, transactionDto.getAmount());
            }
            
            if (transactionDto.getCategory() != null) {
                TransactionService.changeCategory(user, transactionId, transactionDto.getCategory());
            }
            
            // Get updated transaction
            transactions = TransactionService.getUserTransactions(user);
            Transaction updated = null;
            
            for (Transaction transaction : transactions) {
                if (transaction.getId() == transactionId) {
                    updated = transaction;
                    break;
                }
            }
            
            if (updated != null) {
                writeResponse(response, TransactionMapper.INSTANCE.toDto(updated));
            } else {
                writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Transaction updated but could not be retrieved");
            }
        } catch (NumberFormatException e) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid transaction ID");
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating transaction: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = authenticateUser(request, response);
        if (user == null) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Transaction ID is required");
            return;
        }
        
        try {
            long transactionId = Long.parseLong(pathInfo.substring(1));
            
            // Check if transaction exists and belongs to user
            Iterable<Transaction> transactions = TransactionService.getUserTransactions(user);
            boolean found = false;
            
            for (Transaction transaction : transactions) {
                if (transaction.getId() == transactionId) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                writeErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Transaction not found or does not belong to user");
                return;
            }
            
            // Delete transaction
            TransactionService.deleteTransaction(user, transactionId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid transaction ID");
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting transaction: " + e.getMessage());
        }
    }

    private User authenticateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
            return null;
        }
        
        Long userId = (Long) session.getAttribute("userId");
        User user = userService.getUserById(userId);
        
        if (user == null) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return null;
        }
        
        // Set user email for audit
        request.setAttribute("userEmail", user.getEmail());
        
        return user;
    }
}
