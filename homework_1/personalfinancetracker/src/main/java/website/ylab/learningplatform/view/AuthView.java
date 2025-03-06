package website.ylab.learningplatform.view;

public class AuthView {
    public String askEmail() {
        ConsoleView.printLine("Введите email:");
        return ConsoleView.readString();
    }

    public String askPassword() {
        ConsoleView.printLine("Введите пароль:");
        return ConsoleView.readString();
    }

    public String askName() {
        ConsoleView.printLine("Введите имя:");
        return ConsoleView.readString();
    }

    public void showRegistrationSuccess() {
        ConsoleView.printLine("Пользователь успешно зарегистрирован!");
    }

    public void showRegistrationError(String message) {
        ConsoleView.printError("Ошибка регистрации: " + message);
    }

    public void showLoginSuccess() {
        ConsoleView.printLine("Вход выполнен успешно!");
    }

    public void showLoginError(String message) {
        ConsoleView.printError("Ошибка входа: " + message);
    }
}
