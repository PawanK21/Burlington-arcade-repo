package com.burlington.arcade.controller;

import com.burlington.arcade.exception.AuthExceptions.*;
import com.burlington.arcade.model.AuthModels.*;
import com.burlington.arcade.service.AuthService;
import com.burlington.arcade.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RateLimitService rateLimitService;

    // ─────────────────────────────────────── SIGNUP ───────────────────────────────────────
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, HttpServletRequest http) {
        String clientIp = clientIp(http);

        if (!rateLimitService.isSignupAllowed(clientIp)) {
            long mins = rateLimitService.getMinutesUntilReset(clientIp, false);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                    new ErrorResponse("RATE_LIMIT", "Too many signup attempts. Try again in " + mins + " minute(s).", 429));
        }

        try {
            rateLimitService.recordSignupAttempt(clientIp);
            SignupResponse res = authService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("USERNAME_TAKEN", e.getMessage(), 409));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("EMAIL_TAKEN", e.getMessage(), 409));
        } catch (Exception e) {
            log.error("Signup error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("SIGNUP_ERROR", "Could not create account", 500));
        }
    }

    // ─────────────────────────────────────── LOGIN ───────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        String clientIp = clientIp(http);

        if (!rateLimitService.isLoginAllowed(clientIp)) {
            long mins = rateLimitService.getMinutesUntilReset(clientIp, true);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                    new ErrorResponse("RATE_LIMIT", "Too many login attempts. Try again in " + mins + " minute(s).", 429));
        }

        try {
            AuthResponse res = authService.login(request);
            rateLimitService.resetLoginAttempts(clientIp);
            return ResponseEntity.ok(res);
        } catch (BadCredentialsException e) {
            rateLimitService.recordLoginAttempt(clientIp);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("INVALID_CREDENTIALS", "Invalid username/email or password", 401));
        } catch (AccountLockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body(new ErrorResponse("ACCOUNT_LOCKED", e.getMessage(), 423));
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("LOGIN_ERROR", "Login failed", 500));
        }
    }

    // ─────────────────────────────────────── REFRESH ───────────────────────────────────────
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshRequest request) {
        try {
            AuthResponse res = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(res);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("INVALID_TOKEN", e.getMessage(), 401));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("TOKEN_ERROR", "Refresh failed", 401));
        }
    }

    // ─────────────────────────────────────── VALIDATE ───────────────────────────────────────
    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("NO_TOKEN", "No token provided", 401));
        }
        return ResponseEntity.ok().body(java.util.Map.of("valid", true));
    }

    // ─── HELPERS ───
    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
