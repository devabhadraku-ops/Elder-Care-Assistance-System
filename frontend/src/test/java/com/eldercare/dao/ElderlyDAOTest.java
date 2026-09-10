package com.eldercare.dao;

import com.eldercare.models.Elderly;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ElderlyDAOTest {

    @Test
    void testAddAndFindElderly() {

        ElderlyDAO dao = new ElderlyDAO();

        Elderly elderly = new Elderly(
                "Test Person",
                70,
                "9999999999",
                LocalDate.of(1956, 1, 1)
        );

        elderly.setEmail("test@example.com");
        elderly.setAddress("Test Address");

        // Add to database
        boolean added = dao.add(elderly);

        assertTrue(added);
        assertTrue(elderly.getElderlyId() > 0);

        // Find from database
        Elderly found = dao.findById(elderly.getElderlyId());

        assertNotNull(found);
        assertEquals("Test Person", found.getName());
        assertEquals(70, found.getAge());
        assertEquals("9999999999", found.getPhone());

        // Clean up test data
        dao.delete(elderly.getElderlyId());
    }
}