package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public boolean register(User user, String plainPassword) throws Exception {

        if (userDAO.findByEmail(user.getEmail()) != null) return false;
        if (userDAO.findByUsername(user.getUsername()) != null) return false;

        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);

        return userDAO.save(user);
    }

    public User login(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);
        if (user == null) return null;

        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public User getById(int id) throws Exception {
        return userDAO.findById(id);
    }
}