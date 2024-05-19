package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Transactional
    public User createUser(@Valid User user) {

        if (isUsernameTaken(user.username)) {
            throw new ValidationException("Username is already taken");
        }
        if (isEmailTaken(user.email)) {
            throw new ValidationException("Email is already taken");
        }

        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        userRepository.persist(user);
        return user;
    }

    public List<User> listAllUsers() {
        return userRepository.listAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findByIdOptional(id);
    }

    @Transactional
    public User updateUser(Long id, @Valid User user) {
        Optional<User> userOptional = userRepository.findByIdOptional(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (!existingUser.username.equals(user.username) && isUsernameTaken(user.username)) {
                throw new ValidationException("Username is already taken");
            }
            if (!existingUser.email.equals(user.email) && isEmailTaken(user.email)) {
                throw new ValidationException("Email is already taken");
            }
            existingUser.username = user.username;
            existingUser.email = user.email;
            existingUser.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
            userRepository.persist(existingUser);
            return existingUser;
        }
        return null;
    }

    @Transactional
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isEmailTaken(String email) {
        return userRepository.find("email", email).firstResultOptional().isPresent();
    }
}
