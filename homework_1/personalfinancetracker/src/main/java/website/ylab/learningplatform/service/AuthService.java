package website.ylab.learningplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.util.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        User newUser = new User(name, email, PasswordEncoder.encode(password), false, false, new BigDecimal(0));
        userRepository.save(newUser);
        return true;
    }

    public boolean login(String email, String passwordHash) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && user.get().getPasswordHash().equals(passwordHash) && !user.get().getIsBlocked();
    }

    public User loginUser(String email, String passwordHash) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.get();
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
