package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {

    // Add a new alert
    public boolean add(Alert alert) {

        String sql = "INSERT INTO Alerts " +
                "(elderly_id, alert_type, severity, description, " +
                "response_required, response_deadline) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, alert.getElderlyId());
            statement.setString(2, alert.getAlertType());
            statement.setString(3, alert.getSeverity());
            statement.setString(4, alert.getDescription());
            statement.setBoolean(5, alert.isResponseRequired());

            if (alert.getResponseDeadline() != null) {
                statement.setTimestamp(6,
                        Timestamp.valueOf(alert.getResponseDeadline()));
            } else {
                statement.setNull(6, Types.TIMESTAMP);
            }

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        alert.setAlertId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding alert: " + e.getMessage());
        }

        return false;
    }

    // Find alert by ID
    public Alert findById(int id) {

        String sql = "SELECT * FROM Alerts WHERE alert_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAlert(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding alert: " + e.getMessage());
        }

        return null;
    }

    // Find all alerts
    public List<Alert> findAll() {

        List<Alert> alerts = new ArrayList<>();

        String sql = "SELECT * FROM Alerts ORDER BY triggered_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                alerts.add(mapResultSetToAlert(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding alerts: " + e.getMessage());
        }

        return alerts;
    }

    // Find alerts for an elderly person
    public List<Alert> findByElderlyId(int elderlyId) {

        List<Alert> alerts = new ArrayList<>();

        String sql = "SELECT * FROM Alerts " +
                "WHERE elderly_id = ? " +
                "ORDER BY triggered_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    alerts.add(mapResultSetToAlert(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly alerts: "
                    + e.getMessage());
        }

        return alerts;
    }

    // Find unacknowledged alerts
    public List<Alert> findUnacknowledged() {

        List<Alert> alerts = new ArrayList<>();

        String sql = "SELECT * FROM Alerts " +
                "WHERE acknowledged = FALSE " +
                "ORDER BY triggered_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                alerts.add(mapResultSetToAlert(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding unacknowledged alerts: "
                    + e.getMessage());
        }

        return alerts;
    }

    // Acknowledge an alert
    public boolean acknowledge(int alertId, int caregiverId) {

        String sql = "UPDATE Alerts SET " +
                "acknowledged = TRUE, " +
                "acknowledgment_time = CURRENT_TIMESTAMP, " +
                "acknowledgment_by = ? " +
                "WHERE alert_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);
            statement.setInt(2, alertId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error acknowledging alert: "
                    + e.getMessage());
        }

        return false;
    }

    // Delete alert
    public boolean delete(int id) {

        String sql = "DELETE FROM Alerts WHERE alert_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting alert: " + e.getMessage());
        }

        return false;
    }

    // Convert database row into Alert object
    private Alert mapResultSetToAlert(ResultSet rs)
            throws SQLException {

        Timestamp acknowledgmentTimestamp =
                rs.getTimestamp("acknowledgment_time");

        Timestamp triggeredTimestamp =
                rs.getTimestamp("triggered_time");

        Timestamp deadlineTimestamp =
                rs.getTimestamp("response_deadline");

        return new Alert(
                rs.getInt("alert_id"),
                rs.getInt("elderly_id"),
                rs.getString("alert_type"),
                rs.getString("severity"),
                rs.getString("description"),
                rs.getBoolean("acknowledged"),
                acknowledgmentTimestamp != null
                        ? acknowledgmentTimestamp.toLocalDateTime()
                        : null,
                rs.getInt("acknowledgment_by"),
                triggeredTimestamp != null
                        ? triggeredTimestamp.toLocalDateTime()
                        : null,
                rs.getBoolean("response_required"),
                deadlineTimestamp != null
                        ? deadlineTimestamp.toLocalDateTime()
                        : null
        );
    }
}