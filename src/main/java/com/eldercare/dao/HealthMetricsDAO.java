package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.HealthMetrics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HealthMetricsDAO {

    // Add a new health record
    public boolean add(HealthMetrics metrics) {

        String sql = "INSERT INTO HealthMetrics " +
                "(elderly_id, systolic_bp, diastolic_bp, heart_rate, " +
                "temperature, oxygen_level, weight, recorded_by, notes, " +
                "alert_generated, alert_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, metrics.getElderlyId());
            statement.setInt(2, metrics.getSystolicBp());
            statement.setInt(3, metrics.getDiastolicBp());
            statement.setInt(4, metrics.getHeartRate());
            statement.setDouble(5, metrics.getTemperature());
            statement.setInt(6, metrics.getOxygenLevel());
            statement.setDouble(7, metrics.getWeight());
            statement.setString(8, metrics.getRecordedBy());
            statement.setString(9, metrics.getNotes());
            statement.setBoolean(10, metrics.isAlertGenerated());

            if (metrics.getAlertId() > 0) {
                statement.setInt(11, metrics.getAlertId());
            } else {
                statement.setNull(11, Types.INTEGER);
            }

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        metrics.setMetricsId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding health metrics: "
                    + e.getMessage());
        }

        return false;
    }

    // Find health record by ID
    public HealthMetrics findById(int id) {

        String sql = "SELECT * FROM HealthMetrics WHERE metrics_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMetrics(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding health metrics: "
                    + e.getMessage());
        }

        return null;
    }

    // Find health records for an elderly person
    public List<HealthMetrics> findByElderlyId(int elderlyId) {

        List<HealthMetrics> metricsList = new ArrayList<>();

        String sql = "SELECT * FROM HealthMetrics " +
                "WHERE elderly_id = ? " +
                "ORDER BY recorded_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    metricsList.add(mapResultSetToMetrics(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding health records: "
                    + e.getMessage());
        }

        return metricsList;
    }

    // Get the latest health record
    public HealthMetrics findLatest(int elderlyId) {

        String sql = "SELECT * FROM HealthMetrics " +
                "WHERE elderly_id = ? " +
                "ORDER BY recorded_time DESC LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMetrics(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding latest health record: "
                    + e.getMessage());
        }

        return null;
    }

    // Find health records that generated alerts
    public List<HealthMetrics> findAlertGenerated() {

        List<HealthMetrics> metricsList = new ArrayList<>();

        String sql = "SELECT * FROM HealthMetrics " +
                "WHERE alert_generated = TRUE " +
                "ORDER BY recorded_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                metricsList.add(mapResultSetToMetrics(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding alert health records: "
                    + e.getMessage());
        }

        return metricsList;
    }

    // Link a health record to an alert
    public boolean linkAlert(int metricsId, int alertId) {

        String sql = "UPDATE HealthMetrics SET " +
                "alert_generated = TRUE, " +
                "alert_id = ? " +
                "WHERE metrics_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, alertId);
            statement.setInt(2, metricsId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error linking alert: "
                    + e.getMessage());
        }

        return false;
    }

    // Delete health record
    public boolean delete(int id) {

        String sql = "DELETE FROM HealthMetrics WHERE metrics_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting health metrics: "
                    + e.getMessage());
        }

        return false;
    }

    // Convert database row into HealthMetrics object
    private HealthMetrics mapResultSetToMetrics(ResultSet rs)
            throws SQLException {

        Timestamp recordedTime =
                rs.getTimestamp("recorded_time");

        return new HealthMetrics(
                rs.getInt("metrics_id"),
                rs.getInt("elderly_id"),
                rs.getInt("systolic_bp"),
                rs.getInt("diastolic_bp"),
                rs.getInt("heart_rate"),
                rs.getDouble("temperature"),
                rs.getInt("oxygen_level"),
                rs.getDouble("weight"),
                recordedTime != null
                        ? recordedTime.toLocalDateTime()
                        : null,
                rs.getString("recorded_by"),
                rs.getString("notes"),
                rs.getBoolean("alert_generated"),
                rs.getInt("alert_id")
        );
    }
}