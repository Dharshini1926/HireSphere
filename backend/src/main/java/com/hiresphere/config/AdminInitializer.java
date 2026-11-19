package com.hiresphere.config;

import com.hiresphere.entity.Role;
import com.hiresphere.entity.User;
import com.hiresphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail(adminEmail)) {

                User admin = new User();

                admin.setFullName("HireSphere Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println("Admin account created successfully!");
            }
        };
    }
}