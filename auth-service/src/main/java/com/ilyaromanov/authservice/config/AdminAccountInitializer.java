package com.ilyaromanov.authservice.config;

import com.ilyaromanov.authservice.models.entities.User;
import com.ilyaromanov.authservice.models.enums.Role;
import com.ilyaromanov.authservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.password:admin12345}")
    private String adminPassword;

    @Value("${admin.name:Admin}")
    private String adminName;

    @Override
    public void run(String... args) {
        if (userRepository.getUserByEmail(adminEmail) != null) {
            return;
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setName(adminName);
        admin.setAge((byte) 30);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ROLE_ADMIN);
        userRepository.save(admin);

        LOGGER.info("Создана учётная запись администратора: {}", adminEmail);
    }
}
