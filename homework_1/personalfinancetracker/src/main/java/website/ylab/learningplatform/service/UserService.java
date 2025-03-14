package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;

public class UserService {
    private static final UserRepository userRepository = PostgresUserRepository.getInstance();

    public static void userChangeEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public static void userChangeName(User user, String newName) {
        user.setName(newName);
        userRepository.save(user);
    }

    public static void userChangePassword(User user, String newPassword) {
        user.setPasswordHash(newPassword);
        userRepository.save(user);
    }

    public static void userDelete(User user) {
        userRepository.delete(user);
    }
}
