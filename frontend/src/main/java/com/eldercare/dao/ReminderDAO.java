package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.ActivityReminder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAO {

    // Add a new reminder
    public boolean add(ActivityReminder reminder) {

        String sql = "INSERT INTO ActivityReminders " +
                "(elderly_id, activity_type, reminder_time, frequency, " +
                "description) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, reminder.getElderlyId());
            statement.setString(2, reminder.getActivityType());
            statement.setTime(3, Time.valueOf(reminder.getReminderTime()));
            statement.setString(4, reminder.getFrequency());
            statement.setString(5, reminder.getDescription());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        reminder.setReminderId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding reminder: " + e.getMessage());
        }

        return false;
    }

    // Find reminder by ID
    public ActivityReminder findById(int id) {

        String sql = "SELECT * FROM ActivityReminders WHERE reminder_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReminder(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding reminder: " + e.getMessage());
        }

        return null;
    }

    // Find reminders for an elderly person
    public List<ActivityReminder> findByElderlyId(int elderlyId) {

        List<ActivityReminder> reminders = new ArrayList<>();

        String sql = "SELECT * FROM ActivityReminders " +
                "WHERE elderly_id = ? " +
                "AND is_active = TRUE " +
                "ORDER BY reminder_time";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    reminders.add(mapResultSetToReminder(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding reminders: "
                    + e.getMessage());
        }

        return reminders;
    }

    // Find all active reminders
    public List<ActivityReminder> findAllActive() {

        List<ActivityReminder> reminders = new ArrayList<>();

        String sql = "SELECT * FROM ActivityReminders " +
                "WHERE is_active = TRUE " +
                "ORDER BY reminder_time";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                reminders.add(mapResultSetToReminder(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding active reminders: "
                    + e.getMessage());
        }

        return reminders;
    }

    // Mark reminder as completed
    public boolean markCompleted(int reminderId) {

        String sql = "UPDATE ActivityReminders SET " +
                "completed = TRUE " +
                "WHERE reminder_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reminderId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error completing reminder: "
                    + e.getMessage());
        }

        return false;
    }

    // Record that a notification was sent
    public boolean markNotified(int reminderId) {

        String sql = "UPDATE ActivityReminders SET " +
                "last_notified = CURRENT_TIMESTAMP, " +
                "notification_count = notification_count + 1 " +
                "WHERE reminder_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reminderId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error marking reminder as notified: "
                    + e.getMessage());
        }

        return false;
    }

    // Delete reminder
    public boolean delete(int id) {

        String sql = "DELETE FROM ActivityReminders WHERE reminder_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting reminder: "
                    + e.getMessage());
        }

        return false;
    }

    // Convert database row into ActivityReminder object
    private ActivityReminder mapResultSetToReminder(ResultSet rs)
            throws SQLException {

        Time reminderTime = rs.getTime("reminder_time");

        Timestamp lastNotified =
                rs.getTimestamp("last_notified");

        Timestamp createdDate =
                rs.getTimestamp("created_date");

        return new ActivityReminder(
                rs.getInt("reminder_id"),
                rs.getInt("elderly_id"),
                rs.getString("activity_type"),
                reminderTime != null
                        ? reminderTime.toLocalTime()
                        : null,
                rs.getString("frequency"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                lastNotified != null
                        ? lastNotified.toLocalDateTime()
                        : null,
                rs.getInt("notification_count"),
                createdDate != null
                        ? createdDate.toLocalDateTime()
                        : null,
                rs.getBoolean("is_active")
        );
    }
}
