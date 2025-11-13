package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.example.validation.UserValidator;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(User user, String plainPassword) throws Exception {

        if (user == null) return false;
        if (!UserValidator.isValidUsername(user.getUsername())) return false;
        if (!UserValidator.isValidEmail(user.getEmail())) return false;
        if (!UserValidator.isValidPassword(plainPassword)) return false;

        if (userDAO.findByEmail(user.getEmail()) != null) return false;
        if (userDAO.findByUsername(user.getUsername()) != null) return false;

        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);

        return userDAO.save(user);
    }

    public User login(String email, String password) throws Exception {
        if (!UserValidator.isValidEmail(email)) return null;
        if (password == null || password.isEmpty()) return null;
        User user = userDAO.findByEmail(email);
        if (user == null) return null;

        String stored = user.getPasswordHash();
        if (stored != null && stored.startsWith("$2")) {
            if (BCrypt.checkpw(password, stored)) return user;
        } else {
            if (stored != null && stored.equals(password)) return user; // dev fallback for legacy plaintext entries
        }
        return null;
    }

    public User getById(int id) throws Exception {
        return userDAO.findById(id);
    }
}