package website.ylab.learningplatform.service;


import website.ylab.learningplatform.model.User;
import website.ylab.learningplatform.repository.UserRepository;
import website.ylab.learningplatform.repository.impl.PostgresUserRepository;
import website.ylab.learningplatform.util.PasswordEncoder;

import java.util.Base64;
import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository = PostgresUserRepository.getInstance();

    public AuthService() {
    }

    public boolean register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        }
        User newUser = new User(name, email, PasswordEncoder.encode(password));
        userRepository.save(newUser);
        return true;
    }

    public boolean login(String email, String passwordHash) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && user.get().getPasswordHash().equals(passwordHash) && !user.get().getIsBlocked();
    }

}
