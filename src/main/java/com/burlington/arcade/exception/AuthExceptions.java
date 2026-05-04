package com.burlington.arcade.exception;

public class AuthExceptions {

    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String username) {
            super("Username '" + username + "' is already taken");
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String email) {
            super("An account with email '" + email + "' already exists");
        }
    }

    public static class AccountLockedException extends RuntimeException {
        public AccountLockedException(String message) { super(message); }
    }

    public static class TooManyRequestsException extends RuntimeException {
        public TooManyRequestsException(String message) { super(message); }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) { super(message); }
    }
}
