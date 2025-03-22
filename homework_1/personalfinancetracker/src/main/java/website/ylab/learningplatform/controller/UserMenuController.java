package website.ylab.learningplatform.controller;


import website.ylab.learningplatform.model.Category;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.service.BudgetService;
import website.ylab.learningplatform.service.GoalService;
import website.ylab.learningplatform.service.TransactionService;
import website.ylab.learningplatform.service.UserService;
import website.ylab.learningplatform.view.ConsoleView;
import website.ylab.learningplatform.view.TransactionView;
import website.ylab.learningplatform.view.UserView;

import java.math.BigDecimal;
import java.util.Date;

public class UserMenuController {
    /**
     * Show the user menu. This method will be called after the user logged in.
     * It prints the user notification and the menu for the user.
     * The user can choose the following options:
     * <ol>
     * <li>Samemanagement</li>
     * <li>Transactions</li>
     * <li>Budget</li>
     * <li>Goal</li>
     * <li>Stats</li>
     * <li>Return to main menu</li>
     * <li>Exit</li>
     * </ol>
     * The method will then read the user's choice and perform the corresponding action.
     *
     * @param user the user that is logged in
     */
    public static void showUserMenu(User user) {
        UserView.printNotification(user);
        while (true) {
            UserView.printMenu(user);
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    userMenuSelfManagement(user);
                    break;
                case 2:
                    userMenuTransaction(user);
                    break;
                case 3:
                    userMenuBudget(user);
                    break;
                case 4:
                    userMenuGoal(user);
                    break;
                case 5:
                    userMenuStats(user);
                    break;
                case 9:
                    MenuController.runMainMenu();
                case 0:
                    ConsoleView.printBye();
                    System.exit(0);
                    break;
                default:
                    ConsoleView.printErrorSelection();
            }

        }
    }

    /**
     * The user menu for managing their stats.
     * <p>
     * This method provides the user with a menu for managing their stats.
     * The user can view their current balance, view their stats for a given
     * period, view their spendings by category for the current month, or
     * return to the main menu.
     * <p>
     * The user is given a menu with the following options:
     * <ol>
     *   <li>View current balance</li>
     *   <li>View stats for a given period</li>
     *   <li>View spendings by category for the current month</li>
     *   <li>Return to main menu</li>
     * </ol>
     * <p>
     * The method then reads the user's choice and performs the corresponding
     * action.
     *
     * @param user the user that is logged in
     */
    static void userMenuStats(User user) {
        UserView.askStats();
        int choice = ConsoleView.readInt();
        switch (choice) {
            case 1:
                UserView.printUserBalance(user);
                break;
            case 2:
                UserView.askStatsDateFromTo();
                Date from = ConsoleView.readDate();
                Date to = ConsoleView.readDate();
                UserView.printUserStats(TransactionService.getSumOfUserSpendingsForPeriod(user, from, to), TransactionService.getSumOfUserIncomeForPeriod(user, from, to));
                break;
            case 3:
                UserView.printSpendingsByCategory(TransactionService.getSumOfUserSpendingsByCategoryForCurrentMonth(user));
            case 0:
                return;
            default:
                ConsoleView.printErrorSelection();
        }
    }

    /**
     * The user menu for managing their goals.
     * <p>
     * This method provides the user with a menu for managing their goals.
     * The user can view their current goal, change their current goal, or
     * return to the main menu.
     * <p>
     * The user is given a menu with the following options:
     * <ol>
     *   <li>View current goal</li>
     *   <li>Change current goal</li>
     *   <li>Return to main menu</li>
     * </ol>
     * <p>
     * The method then reads the user's choice and performs the corresponding
     * action.
     *
     * @param user the user that is logged in
     */
    static void userMenuGoal(User user) {
        UserView.printGoalMenu(user);
        int choice = ConsoleView.readInt();
        switch (choice) {
            case 1:
                UserView.printCurrentGoalStats(GoalService.getGoalByUserId(user.getId()), TransactionService.getSumOfUserTransactionsForCurrentMonth(user));
                break;
            case 2:
                UserView.askGoal();
                BigDecimal newGoal = ConsoleView.readBigDecimal();
                GoalService.setGoal(user, newGoal);
                break;
            case 0:
                return;
            default:
                ConsoleView.printErrorSelection();
        }
    }

    /**
     * The user menu for budgets.
     * <p>
     * This method provides the user with a menu for managing their budgets.
     * The user can view their current budget, change their current budget, or
     * return to the main menu.
     * <p>
     * The user is given a menu with the following options:
     * <ol>
     *   <li>View current budget</li>
     *   <li>Change current budget</li>
     *   <li>Return to main menu</li>
     * </ol>
     * <p>
     * The method then reads the user's choice and performs the corresponding
     * action.
     *
     * @param user the user that is logged in
     */
    static void userMenuBudget(User user) {
        UserView.printBudgetMenu(user);
        int choice = ConsoleView.readInt();
        switch (choice) {
            case 1:
                UserView.printCurrentBudget(BudgetService.getBudget(user));
                break;
            case 2:
                UserView.askBudget();
                BigDecimal newBudget = ConsoleView.readBigDecimal();
                BudgetService.setBudget(user, newBudget);
                break;
            case 0:
                return;
            default:
                ConsoleView.printErrorSelection();
        }
    }

    /**
     * The user menu for self management.
     * <p>
     * This method provides the user with a menu for managing their own user data.
     * The user can change their name, email, password, or delete their account.
     * The user can also return to the main menu.
     *
     * @param user the user that is logged in
     */
    public static void userMenuSelfManagement(User user) {
        while (true) {
            UserView.printSelfManagementMenu(user);
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    String newName = ConsoleView.readString();
                    UserService.userChangeName(user, newName);
                    break;
                case 2:
                    String newEmail = ConsoleView.readString();
                    UserService.userChangeEmail(user, newEmail);

                    break;
                case 3:
                    String newPassword = ConsoleView.readString();
                    UserService.userChangePassword(user, newPassword);
                    break;
                case 4:
                    UserService.userDelete(user);
                    break;
                case 0:
                    return;
                default:
                    ConsoleView.printErrorSelection();
            }
        }
    }

    /**
     * The user menu for transactions.
     * <p>
     * This method provides the user with a menu for managing their transactions.
     * The user can create a new transaction, view their transactions, change a
     * transaction or delete a transaction. The user can also return to the main
     * menu.
     *
     * @param user the user that is logged in
     */
    public static void userMenuTransaction(User user) {
        while (true) {
            UserView.printTransactionMenu(user);
            int choice = ConsoleView.readInt();
            switch (choice) {
                case 1:
                    UserView.askTransactionIncome();
                    boolean isIncome = ConsoleView.readBool();
                    UserView.askDescription();
                    String description = ConsoleView.readString();
                    UserView.askAmount();
                    BigDecimal amount = ConsoleView.readBigDecimal();
                    Category category;
                    if (!isIncome) {
                        UserView.askCategory();
                        category = ConsoleView.readCategory();
                    } else category = Category.INCOME;
                    Date date = new Date();
                    try {
                        TransactionService.newTransaction(user, isIncome, amount, category, date, description);
                    } catch (Exception e) {
                        ConsoleView.printError(e.getMessage());
                        return;
                    }
                    UserView.printSuccess();
                    showUserMenu(user);
                    break;
                case 2:
                    TransactionView.printTransactions(TransactionService.getUserTransactions(user));
                    break;
                case 3:
                    if (TransactionService.getUserTransactions(user) == null) {
                        UserView.printErrorNoTransactions();
                        return;
                    }
                    TransactionView.printTransactions(TransactionService.getUserTransactions(user));
                    UserView.askTransactionId();
                    long transactionId = ConsoleView.readLong();
                    UserView.askWhatToChange();
                    int whatToChange = ConsoleView.readInt();
                    switch (whatToChange) {
                        case 1:
                            UserView.askDescription();
                            String newDescription = ConsoleView.readString();
                            TransactionService.changeDescription(user, transactionId, newDescription);
                            break;
                        case 2:
                            UserView.askAmount();
                            BigDecimal newAmount = ConsoleView.readBigDecimal();
                            TransactionService.changeAmount(user, transactionId, newAmount);
                            break;
                        case 3:
                            UserView.askCategory();
                            Category newCategory = ConsoleView.readCategory();
                            TransactionService.changeCategory(user, transactionId, newCategory);
                            break;
                    }
                    break;
                case 4:
                    if (TransactionService.getUserTransactions(user) == null) {
                        UserView.printErrorNoTransactions();
                        return;
                    }
                    TransactionView.printTransactions(TransactionService.getUserTransactions(user));
                    UserView.askTransactionId();
                    long transactionToDeleteId = ConsoleView.readLong();
                    TransactionService.deleteTransaction(user, transactionToDeleteId);
                    break;
                case 0:
                    return;
                default:
                    ConsoleView.printErrorSelection();
            }
        }
    }
}