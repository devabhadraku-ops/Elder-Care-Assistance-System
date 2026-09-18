package com.eldercare.dao;

import com.eldercare.models.Alert;
import com.eldercare.models.Elderly;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AlertDAOTest {

    @Test
    void testAddAndFindAlert() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        AlertDAO alertDAO = new AlertDAO();

        // Create elderly person for the foreign key
        Elderly elderly = new Elderly(
                "Alert Test Elderly",
                70,
                "9999999996",
                LocalDate.of(1956, 1, 1)
        );

        boolean elderlyAdded = elderlyDAO.add(elderly);

        assertTrue(elderlyAdded);
        assertTrue(elderly.getElderlyId() > 0);

        // Create alert
        Alert alert = new Alert(
                elderly.getElderlyId(),
                "Health Alert",
                "HIGH",
                "Test health alert"
        );

        boolean added = alertDAO.add(alert);

        assertTrue(added);
        assertTrue(alert.getAlertId() > 0);

        // Find alert
        Alert found = alertDAO.findById(alert.getAlertId());

        assertNotNull(found);
        assertEquals("Health Alert", found.getAlertType());
        assertEquals("HIGH", found.getSeverity());
        assertEquals("Test health alert", found.getDescription());

        // Clean up
        alertDAO.delete(alert.getAlertId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}