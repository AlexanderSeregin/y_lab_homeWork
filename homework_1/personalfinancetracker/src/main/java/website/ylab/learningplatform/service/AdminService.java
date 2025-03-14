package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;

public class AdminService {
    private static final UserRepository userRepository = PostgresUserRepository.getInstance();

    public static void blockUser(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        user.setBlocked(true);
        userRepository.save(user);
    }
}
