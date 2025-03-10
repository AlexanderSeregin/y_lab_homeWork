package website.ylab.learningplatform.view;

import website.ylab.learningplatform.model.Transaction;

public class TransactionView {
    public static void printTransactions(Iterable<Transaction> transactions) {
        ConsoleView.printLine("Transactions:");
        if (transactions == null) {
            ConsoleView.printLine("Нет транзакций");
            return;
        }
        transactions.forEach(t -> ConsoleView.printLine(t.toString(t)));
    }

}
