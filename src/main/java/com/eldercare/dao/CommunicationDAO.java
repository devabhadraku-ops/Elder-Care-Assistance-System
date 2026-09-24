package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.Communication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommunicationDAO {

    // Add a new communication
    public boolean add(Communication communication) {

        String sql = """
                INSERT INTO communications
                (elderly_id, caregiver_id, message_type, message, is_read)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, communication.getElderlyId());
            statement.setInt(2, communication.getCaregiverId());
            statement.setString(3, communication.getMessageType());
            statement.setString(4, communication.getMessage());
            statement.setBoolean(5, communication.isRead());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    communication.setCommunicationId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Find communication by ID
    public Communication findById(int communicationId) {

        String sql = """
                SELECT *
                FROM communications
                WHERE communication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, communicationId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToCommunication(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Find communications for an elderly person
    public List<Communication> findByElderlyId(int elderlyId) {

        List<Communication> communications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM communications
                WHERE elderly_id = ?
                ORDER BY sent_time DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    communications.add(
                            mapResultSetToCommunication(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return communications;
    }


    // Find communications for a caregiver
    public List<Communication> findByCaregiverId(int caregiverId) {

        List<Communication> communications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM communications
                WHERE caregiver_id = ?
                ORDER BY sent_time DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    communications.add(
                            mapResultSetToCommunication(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return communications;
    }


    // Find unread communications for a caregiver
    public List<Communication> findUnreadByCaregiverId(int caregiverId) {

        List<Communication> communications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM communications
                WHERE caregiver_id = ?
                  AND is_read = FALSE
                ORDER BY sent_time DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    communications.add(
                            mapResultSetToCommunication(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return communications;
    }


    // Mark communication as read
    public boolean markAsRead(int communicationId) {

        String sql = """
                UPDATE communications
                SET is_read = TRUE,
                    read_time = CURRENT_TIMESTAMP
                WHERE communication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, communicationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Delete communication
    public boolean delete(int communicationId) {

        String sql = """
                DELETE FROM communications
                WHERE communication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, communicationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Convert database row into Communication object
    private Communication mapResultSetToCommunication(
            ResultSet resultSet) throws SQLException {

        Timestamp sentTimestamp =
                resultSet.getTimestamp("sent_time");

        Timestamp readTimestamp =
                resultSet.getTimestamp("read_time");

        return new Communication(
                resultSet.getInt("communication_id"),
                resultSet.getInt("elderly_id"),
                resultSet.getInt("caregiver_id"),
                resultSet.getString("message_type"),
                resultSet.getString("message"),
                sentTimestamp != null
                        ? sentTimestamp.toLocalDateTime()
                        : null,
                readTimestamp != null
                        ? readTimestamp.toLocalDateTime()
                        : null,
                resultSet.getBoolean("is_read")
        );
    }
}