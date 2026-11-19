
package com.hiresphere.service;

import com.hiresphere.dto.RegisterRequest;
import com.hiresphere.entity.Role;
import com.hiresphere.entity.User;
import com.hiresphere.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hiresphere.dto.LoginRequest;
import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest request) {

        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin registration is not allowed"
            );
        }

        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        User user = new User();

        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public User loginUser(LoginRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"
                        ));

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return user;
    }

}
