package com.eldercare.dao;

import com.eldercare.models.ActivityReminder;
import com.eldercare.models.Elderly;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReminderDAOTest {

    @Test
    void testAddAndFindReminder() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        ReminderDAO reminderDAO = new ReminderDAO();

        // Create an elderly person for the foreign key
        Elderly elderly = new Elderly(
                "Reminder Test Elderly",
                70,
                "9999999997",
                LocalDate.of(1956, 1, 1)
        );

        boolean elderlyAdded = elderlyDAO.add(elderly);

        assertTrue(elderlyAdded);
        assertTrue(elderly.getElderlyId() > 0);

        // Create reminder for that elderly person
        ActivityReminder reminder = new ActivityReminder(
                elderly.getElderlyId(),
                "Take Medicine",
                LocalTime.of(10, 0),
                "Daily",
                "Take morning medicine"
        );

        boolean added = reminderDAO.add(reminder);

        assertTrue(added);
        assertTrue(reminder.getReminderId() > 0);

        // Find reminder
        ActivityReminder found =
                reminderDAO.findById(reminder.getReminderId());

        assertNotNull(found);
        assertEquals("Take Medicine", found.getActivityType());

        // Clean up reminder first
        reminderDAO.delete(reminder.getReminderId());

        // Clean up elderly person
        elderlyDAO.delete(elderly.getElderlyId());
    }
}