package com.burlington.arcade.service;

import com.burlington.arcade.entity.User;
import com.burlington.arcade.exception.AuthExceptions.*;
import com.burlington.arcade.model.AuthModels.*;
import com.burlington.arcade.repository.UserRepository;
import com.burlington.arcade.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS_BEFORE_LOCK = 5;
    private static final long LOCK_DURATION_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // ─────────────────────────────────────── SIGNUP ───────────────────────────────────────
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // Normalize input
        String username = request.getUsername().trim().toLowerCase();
        String email = request.getEmail().trim().toLowerCase();

        // Check uniqueness
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        // Create with hashed password (BCrypt strength 12)
        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName() != null ? request.getFullName().trim() : null)
                .company(request.getCompany() != null ? request.getCompany().trim() : null)
                .role(User.Role.PROSPECT)  // default — staff manually upgraded by admin
                .accountLocked(false)
                .enabled(true)
                .failedLoginAttempts(0)
                .build();

        User saved = userRepository.save(user);
        log.info("New user registered: username={} email={}", saved.getUsername(), saved.getEmail());

        return SignupResponse.builder()
                .success(true)
                .message("Account created successfully. You may now log in.")
                .username(saved.getUsername())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // ─────────────────────────────────────── LOGIN ───────────────────────────────────────
    @Transactional
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getUsernameOrEmail().trim().toLowerCase();

        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // Check lockout
        if (user.isAccountLocked()) {
            if (user.getLockTime() != null) {
                long mins = ChronoUnit.MINUTES.between(user.getLockTime(), Instant.now());
                if (mins >= LOCK_DURATION_MINUTES) {
                    // Auto-unlock after lockout duration expires
                    user.setAccountLocked(false);
                    user.setFailedLoginAttempts(0);
                    user.setLockTime(null);
                    userRepository.save(user);
                } else {
                    long remaining = LOCK_DURATION_MINUTES - mins;
                    throw new AccountLockedException(
                            "Account is locked due to too many failed login attempts. " +
                            "Try again in " + remaining + " minute(s).");
                }
            }
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("Account is disabled. Contact support.");
        }

        // Authenticate via Spring Security (verifies BCrypt password)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            handleFailedLogin(user);
            throw new BadCredentialsException("Invalid credentials");
        }

        // Success — reset attempts, update last login
        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        user.setLastLogin(Instant.now());
        userRepository.save(user);

        // Issue tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("User logged in: username={}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .expiresIn(3600000L)
                .build();
    }

    // ─────────────────────────────────────── REFRESH ───────────────────────────────────────
    public AuthResponse refresh(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        if (username == null) throw new InvalidTokenException("Invalid refresh token");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidTokenException("User no longer exists"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }
        if (user.isAccountLocked() || !user.isEnabled()) {
            throw new InvalidTokenException("Account is no longer active");
        }

        String newAccessToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .expiresIn(3600000L)
                .build();
    }

    // ─────────────────────────────────────── HELPERS ───────────────────────────────────────
    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS_BEFORE_LOCK) {
            user.setAccountLocked(true);
            user.setLockTime(Instant.now());
            log.warn("Account locked after {} failed attempts: username={}", attempts, user.getUsername());
        }
        userRepository.save(user);
    }
}
