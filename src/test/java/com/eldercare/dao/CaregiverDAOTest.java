package com.eldercare.dao;

import com.eldercare.models.Caregiver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverDAOTest {

    @Test
    void testAddAndFindCaregiver() {

        CaregiverDAO dao = new CaregiverDAO();

        Caregiver caregiver = new Caregiver(
                "Test Caregiver",
                "9999999998"
        );

        caregiver.setEmail("caregiver@example.com");

        // Add to database
        boolean added = dao.add(caregiver);

        assertTrue(added);
        assertTrue(caregiver.getCaregiverId() > 0);

        // Find from database
        Caregiver found = dao.findById(caregiver.getCaregiverId());

        assertNotNull(found);
        assertEquals("Test Caregiver", found.getName());
        assertEquals("9999999998", found.getPhone());

        // Clean up
        dao.delete(caregiver.getCaregiverId());
    }
}