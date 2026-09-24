package com.eldercare.service;

import com.eldercare.dao.UserDAO;
import com.eldercare.models.User;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Creates a new user with a securely hashed password.
     */
    public boolean registerUser(String username, String password, String role) {

        if (username == null || username.isBlank()) {
            return false;
        }

        if (password == null || password.isBlank()) {
            return false;
        }

        if (role == null || role.isBlank()) {
            return false;
        }

        // Check if username already exists
        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        // Hash the password before storing it
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(username, passwordHash, role);

        return userDAO.add(user);
    }

    /**
     * Checks username and password and returns the logged-in user.
     * Returns null if login fails.
     */
    public User login(String username, String password) {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            return null;
        }

        User user = userDAO.findByUsername(username);

        if (user == null) {
            return null;
        }

        // Do not allow inactive accounts to log in
        if (!user.isActive()) {
            return null;
        }

        // Compare entered password with stored BCrypt hash
        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            return null;
        }

        return user;
    }

    /**
     * Deactivates a user account.
     */
    public boolean deactivateUser(int userId) {
        User user = userDAO.findById(userId);

        if (user == null) {
            return false;
        }

        user.setActive(false);

        return userDAO.update(user);
    }

    /**
     * Reactivates a user account.
     */
    public boolean activateUser(int userId) {
        User user = userDAO.findById(userId);

        if (user == null) {
            return false;
        }

        user.setActive(true);

        return userDAO.update(user);
    }
}