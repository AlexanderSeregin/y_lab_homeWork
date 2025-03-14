package website.ylab.learningplatform.service;


import website.ylab.learningplatform.repository.impl.PostgresUserRepository;

public class AdminService {
    public static void blockUser(long userId) {
        PostgresUserRepository.getInstance().findById(userId).ifPresent(user -> user.setIsBlocked(true));
    }
}
