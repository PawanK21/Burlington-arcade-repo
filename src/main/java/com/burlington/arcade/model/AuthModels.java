package com.burlington.arcade.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

public class AuthModels {

    // ─── LOGIN ───
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Username or email is required")
        @Size(min = 3, max = 120, message = "Username/email must be between 3 and 120 characters")
        private String usernameOrEmail;

        @NotBlank(message = "Password is required")
        @Size(min = 1, max = 100, message = "Password too long")
        private String password;
    }

    // ─── SIGNUP ───
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_.-]+$", message = "Username can only contain letters, numbers, dots, underscores, hyphens")
        private String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 120, message = "Email too long")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+=\\-]).+$",
            message = "Password must contain uppercase, lowercase, number, and special character"
        )
        private String password;

        @Size(max = 120)
        private String fullName;

        @Size(max = 120)
        private String company;
    }

    // ─── REFRESH ───
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshRequest {
        @NotBlank
        private String refreshToken;
    }

    // ─── AUTH RESPONSE ───
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private String username;
        private String email;
        private String fullName;
        private String role;
        private long expiresIn;
    }

    // ─── SIGNUP RESPONSE ───
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupResponse {
        private boolean success;
        private String message;
        private String username;
        private Instant createdAt;
    }

    // ─── ERROR RESPONSE ───
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String error;
        private String message;
        private int status;
        private Instant timestamp = Instant.now();

        public ErrorResponse(String error, String message, int status) {
            this.error = error;
            this.message = message;
            this.status = status;
            this.timestamp = Instant.now();
        }
    }
}
