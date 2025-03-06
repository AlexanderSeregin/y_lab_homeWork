package website.ylab.learningplatform.controller;

import website.ylab.learningplatform.datasource.UserDao;
import website.ylab.learningplatform.service.AuthService;
import website.ylab.learningplatform.view.AuthView;

public class AuthController {
    private final AuthService authService;
    private final AuthView authView; // View для аутентификации

    public AuthController() {
        this.authService = new AuthService();
        this.authView = new AuthView();
    }



    public void register() {
        // Контроллер просит View показать форму регистрации
        String email = authView.askEmail();
        String password = authView.askPassword();
        String name = authView.askName();

        // Контроллер вызывает метод из Model (через AuthService)
        boolean result = authService.register(name, email, password);

        // В зависимости от результата выводим пользователю нужное сообщение
        if (result) {
            authView.showRegistrationSuccess();
        } else {
            authView.showRegistrationError("Email уже зарегистрирован!");
        }
    }

    public void login() {
        String email = authView.askEmail();
        String password = authView.askPassword();

        boolean loggedIn = authService.login(email, password);
        if (loggedIn) {
            authView.showLoginSuccess();
            UserController.run(UserDao.getInstance().findByEmail(email));
        } else {
            authView.showLoginError("Неверный email или пароль!");
        }
    }
}
