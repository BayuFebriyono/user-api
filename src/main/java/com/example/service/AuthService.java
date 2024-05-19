package com.example.service;

import com.example.TokenService;
import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;



    @Inject
    TokenService service;

    @Transactional
    public Optional<String> authenticate(String username, String password) throws Exception {
        Optional<User> userOptional = userRepository.find("username", username).firstResultOptional();

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.loginAttempt >= 3) {
                throw new Exception("Maximum login attempts exceeded");
            }

            if (BCrypt.checkpw(password, user.password)) {
                user.lastLogin = java.time.LocalDateTime.now();
                user.loginAttempt = 0;
                user.persist();

                return Optional.of(service.generateAdminToken(user.email, user.username));
            } else {
                user.loginAttempt++;
                user.persist();
            }
        }

        return Optional.empty();
    }

    @Transactional
    public User register(User user) {
        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        user.persist();
        return user;
    }
}
