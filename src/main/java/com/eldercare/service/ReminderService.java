package com.eldercare.service;

import com.eldercare.dao.ReminderDAO;
import com.eldercare.models.ActivityReminder;

import java.util.List;

public class ReminderService {

    private final ReminderDAO reminderDAO;

    public ReminderService() {
        this.reminderDAO = new ReminderDAO();
    }

    // Add a new reminder
    public boolean addReminder(ActivityReminder reminder) {

        if (reminder == null) {
            return false;
        }

        if (!reminder.isValid()) {
            return false;
        }

        return reminderDAO.add(reminder);
    }

    // Find reminder by ID
    public ActivityReminder getReminderById(int id) {

        if (id <= 0) {
            return null;
        }

        return reminderDAO.findById(id);
    }

    // Get reminders for an elderly person
    public List<ActivityReminder> getRemindersByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return reminderDAO.findByElderlyId(elderlyId);
    }

    // Get all active reminders
    public List<ActivityReminder> getActiveReminders() {
        return reminderDAO.findAllActive();
    }

    // Mark reminder as completed
    public boolean completeReminder(int reminderId) {

        if (reminderId <= 0) {
            return false;
        }

        return reminderDAO.markCompleted(reminderId);
    }

    // Mark reminder as notified
    public boolean notifyReminder(int reminderId) {

        if (reminderId <= 0) {
            return false;
        }

        return reminderDAO.markNotified(reminderId);
    }

    // Delete a reminder
    public boolean deleteReminder(int reminderId) {

        if (reminderId <= 0) {
            return false;
        }

        return reminderDAO.delete(reminderId);
    }
}