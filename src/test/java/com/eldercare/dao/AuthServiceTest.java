package com.eldercare.service;

import com.eldercare.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void testRegisterAndLogin() {
        AuthService authService = new AuthService();

        String username = "testuser_" + System.currentTimeMillis();
        String password = "Test@123";
        String role = "CAREGIVER";

        boolean registered = authService.registerUser(username, password, role);

        assertTrue(registered);

        User loggedInUser = authService.login(username, password);

        assertNotNull(loggedInUser);
        assertEquals(username, loggedInUser.getUsername());
        assertEquals(role, loggedInUser.getRole());
    }

    @Test
    void testLoginWithWrongPassword() {
        AuthService authService = new AuthService();

        String username = "wrongpass_" + System.currentTimeMillis();
        String password = "Correct@123";

        assertTrue(
                authService.registerUser(username, password, "CAREGIVER")
        );

        User loggedInUser = authService.login(username, "WrongPassword");

        assertNull(loggedInUser);
    }
}