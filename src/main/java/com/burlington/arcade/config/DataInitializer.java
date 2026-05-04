package com.burlington.arcade.config;

import com.burlington.arcade.entity.User;
import com.burlington.arcade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Seeds default demo accounts on first startup.
     * Skipped if any user already exists in the database.
     */
    @Bean
    public CommandLineRunner seedDefaultUsers() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("Users already exist — skipping seed");
                return;
            }

            createUser("admin", "admin@burlingtonarcade.com",
                    "Admin@Burlington2024", "Administrator", "Burlington Arcade", User.Role.ADMIN);

            createUser("salesrep", "sales@burlingtonarcade.com",
                    "Burlington@2024", "Sales Representative", "Burlington Arcade", User.Role.SALES);

            createUser("prospect", "prospect@example.com",
                    "Prospect@2024", "Demo Prospect", "Demo Co.", User.Role.PROSPECT);

            log.info("Seeded 3 default users");
        };
    }

    private void createUser(String username, String email, String password,
                            String fullName, String company, User.Role role) {
        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .fullName(fullName)
                .company(company)
                .role(role)
                .accountLocked(false)
                .enabled(true)
                .failedLoginAttempts(0)
                .build();
        userRepository.save(user);
    }
}
