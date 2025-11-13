package org.example.validation;

public final class UserValidator {
    private UserValidator() {}

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.length() <= 32;
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
