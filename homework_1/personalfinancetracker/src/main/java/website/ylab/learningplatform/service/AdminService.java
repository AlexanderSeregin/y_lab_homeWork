package website.ylab.learningplatform.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;

@Service
public class AdminService {
    private static UserRepository userRepository;

    @Autowired
    public AdminService(UserRepository userRepository) {
        AdminService.userRepository = userRepository;
    }


    public static void blockUser(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        user.setBlocked(true);
        userRepository.save(user);
    }
}
