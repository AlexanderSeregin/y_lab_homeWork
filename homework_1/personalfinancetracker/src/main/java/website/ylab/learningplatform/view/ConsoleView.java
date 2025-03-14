package website.ylab.learningplatform.view;

import website.ylab.learningplatform.model.Category;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ConsoleView {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printLine(String text) {
        System.out.println(text);
    }

    public static void printError(String text) {
        System.err.println(text);
    }

    public static int readInt() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            printError("Выберите 1 из пунктов меню.");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    public static String readString() {
        return scanner.nextLine();
    }

    public static boolean readBool() {
        String value = readString();
        return value.equalsIgnoreCase("да") || value.equalsIgnoreCase("yes");
    }

    public static BigDecimal readBigDecimal() {
        while (!scanner.hasNextBigDecimal()) {
            scanner.next();
            printError("Введите число.");
        }
        BigDecimal value = scanner.nextBigDecimal();
        scanner.nextLine();
        return value;
    }

    public static Category readCategory() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            printError("Выберите 1 из пунктов меню.");
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return Category.values()[value];
    }

    public static long readLong() {
        while (!scanner.hasNextLong()) {
            scanner.next();
            printError("Введите число.");
        }
        long value = scanner.nextLong();
        scanner.nextLine();
        return value;
    }

    public static void printErrorSelection() {
        printError("Выберите 1 из пунктов меню.");
    }

    public static void printBye() {
        printLine("До свидания!");
    }

    public static Date readDate() {
        while (!scanner.hasNext("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            scanner.next();
            printError("Введите дату в формате dd.mm.yyyy.");
        }
        String dateString = scanner.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date value;
        try {
            value = dateFormat.parse(dateString);
        } catch (ParseException e) {
            printError("Неверный формат даты. Попробуйте снова.");
            return readDate();
        }
        scanner.nextLine();
        return value;
    }
}
