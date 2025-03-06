package website.ylab.learningplatform.view;

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
            printError("Нужно ввести число!");
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // считываем остаток строки
        return value;
    }

    public static String readString() {
        return scanner.nextLine();
    }
}
