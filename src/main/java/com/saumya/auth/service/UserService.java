package com.saumya.auth.service;

import com.saumya.auth.entity.User;
import com.saumya.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String password) {
        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setVerified(false);
        return userRepository.save(user);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPasswordHash());
    }

    public void markVerified(User user) {
        user.setVerified(true);
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
