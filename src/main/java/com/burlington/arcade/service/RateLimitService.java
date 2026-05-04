package com.burlington.arcade.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory rate limiter to protect login & signup from brute-force attacks.
 * Key: client IP address. Value: attempt count + window-start timestamp.
 *
 * In production, replace with Redis-backed Bucket4j for distributed deployments.
 */
@Service
public class RateLimitService {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int MAX_SIGNUP_ATTEMPTS = 3;
    private static final long WINDOW_MINUTES = 15;

    private static class Bucket {
        int count;
        Instant windowStart;
        Bucket() { this.count = 0; this.windowStart = Instant.now(); }
    }

    private final Map<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> signupBuckets = new ConcurrentHashMap<>();

    public boolean isLoginAllowed(String clientIp) {
        return checkBucket(loginBuckets, clientIp, MAX_LOGIN_ATTEMPTS);
    }

    public boolean isSignupAllowed(String clientIp) {
        return checkBucket(signupBuckets, clientIp, MAX_SIGNUP_ATTEMPTS);
    }

    public void recordLoginAttempt(String clientIp) {
        recordAttempt(loginBuckets, clientIp);
    }

    public void recordSignupAttempt(String clientIp) {
        recordAttempt(signupBuckets, clientIp);
    }

    public void resetLoginAttempts(String clientIp) {
        loginBuckets.remove(clientIp);
    }

    public long getMinutesUntilReset(String clientIp, boolean isLogin) {
        Bucket b = (isLogin ? loginBuckets : signupBuckets).get(clientIp);
        if (b == null) return 0;
        long elapsed = ChronoUnit.MINUTES.between(b.windowStart, Instant.now());
        return Math.max(WINDOW_MINUTES - elapsed, 1);
    }

    // ─── INTERNAL ───
    private boolean checkBucket(Map<String, Bucket> buckets, String key, int max) {
        Bucket b = buckets.get(key);
        if (b == null) return true;

        // Reset if window expired
        if (ChronoUnit.MINUTES.between(b.windowStart, Instant.now()) >= WINDOW_MINUTES) {
            buckets.remove(key);
            return true;
        }
        return b.count < max;
    }

    private void recordAttempt(Map<String, Bucket> buckets, String key) {
        buckets.compute(key, (k, b) -> {
            if (b == null || ChronoUnit.MINUTES.between(b.windowStart, Instant.now()) >= WINDOW_MINUTES) {
                Bucket fresh = new Bucket();
                fresh.count = 1;
                return fresh;
            }
            b.count++;
            return b;
        });
    }
}
