package website.ylab.learningplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.util.PasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public void updateName(User user, String newName) {
        user.setName(newName);
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(PasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
