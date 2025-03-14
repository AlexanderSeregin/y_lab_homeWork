package website.ylab.learningplatform.controller;


import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;
import website.ylab.learningplatform.service.AuthService;
import website.ylab.learningplatform.util.PasswordEncoder;
import website.ylab.learningplatform.view.AuthView;

public class AuthController {
    private final AuthService authService;
    private final AuthView authView; // View для аутентификации

    public AuthController() {
        this.authService = new AuthService();
        this.authView = new AuthView();
    }

    /**
     * Method to register a user.
     *
     * <p>Ask the user to enter their name, email and password.
     * Then call the AuthService to register the user.
     * If the registration was successful, show a success message.
     * If the email is already registered, show an error message.
     * </p>
     *
     */
    public void register() {
        // Контроллер просит View показать форму регистрации
        String name = authView.askName();
        String email = authView.askEmail();
        String password = authView.askPassword();


        // Контроллер вызывает метод из Model (через AuthService)
        boolean result = authService.register(name, email, password);

        // В зависимости от результата выводим пользователю нужное сообщение
        if (result) {
            authView.showRegistrationSuccess();
        } else {
            authView.showRegistrationErrorEmailAlreadyRegistered();
        }
    }

    /**
     * Method to login a user.
     *
     * <p>Ask the user to enter their email and password.
     * Then call the AuthService to login the user.
     * If the login was successful, show a success message and
     * redirect the user to the appropriate menu.
     * If the email or password are incorrect, show an error message.
     * </p>
     */
    public void login() {
        String email = authView.askEmail();
        String password = authView.askPassword();
        String passwordHash = PasswordEncoder.encode(password);
        boolean loggedIn = authService.login(email, passwordHash);
        if (loggedIn) {
            authView.showLoginSuccess();
            User user = PostgresUserRepository.getInstance().findByEmail(email).get();
            if (user.isAdmin())
                AdminMenuController.showAdminMenu();
            else UserMenuController.showUserMenu(user);
        } else {
            authView.showLoginErrorWrongCredentials();
        }
    }
}
