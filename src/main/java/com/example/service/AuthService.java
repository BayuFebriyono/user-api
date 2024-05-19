package com.example.service;

import com.example.TokenService;
import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;
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

                if(Objects.equals(user.role, "admin")) return Optional.of(service.generateAdminToken(user.email, user.username));
                return Optional.of(service.generateUserToken(user.email, user.username));
            } else {
                user.loginAttempt++;
                user.persist();
            }
        }

        return Optional.empty();
    }

    @Transactional
    public User register(User user) {

        if ( isUsernameTaken(user.username)) {
            throw new ValidationException("Username is already taken");
        }
        if (isEmailTaken(user.email)) {
            throw new ValidationException("Email is already taken");
        }

        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        user.role = "user";
        user.persist();
        return user;
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isEmailTaken(String email) {
        return userRepository.find("email", email).firstResultOptional().isPresent();
    }
}
