package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.ServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {

    // Add a new service request
    public boolean add(ServiceRequest request) {

        String sql = "INSERT INTO ServiceRequests " +
                "(elderly_id, service_type, description, urgency, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, request.getElderlyId());
            statement.setString(2, request.getServiceType());
            statement.setString(3, request.getDescription());
            statement.setString(4, request.getUrgency());
            statement.setString(5, request.getStatus());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        request.setRequestId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding service request: "
                    + e.getMessage());
        }

        return false;
    }

    // Find request by ID
    public ServiceRequest findById(int id) {

        String sql = "SELECT * FROM ServiceRequests WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRequest(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding service request: "
                    + e.getMessage());
        }

        return null;
    }

    // Find requests for an elderly person
    public List<ServiceRequest> findByElderlyId(int elderlyId) {

        List<ServiceRequest> requests = new ArrayList<>();

        String sql = "SELECT * FROM ServiceRequests " +
                "WHERE elderly_id = ? " +
                "ORDER BY request_time DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly requests: "
                    + e.getMessage());
        }

        return requests;
    }

    // Find pending requests
    public List<ServiceRequest> findPending() {

        List<ServiceRequest> requests = new ArrayList<>();

        String sql = "SELECT * FROM ServiceRequests " +
                "WHERE status = 'PENDING' " +
                "ORDER BY request_time";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding pending requests: "
                    + e.getMessage());
        }

        return requests;
    }

    // Assign a caregiver
    public boolean assignCaregiver(int requestId, int caregiverId) {

        String sql = "UPDATE ServiceRequests SET " +
                "assigned_caregiver_id = ?, " +
                "status = 'ASSIGNED', " +
                "assigned_time = CURRENT_TIMESTAMP " +
                "WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);
            statement.setInt(2, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error assigning caregiver: "
                    + e.getMessage());
        }

        return false;
    }

    // Update request status
    public boolean updateStatus(int requestId, String status) {

        String sql = "UPDATE ServiceRequests SET status = ? " +
                "WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating request status: "
                    + e.getMessage());
        }

        return false;
    }

    // Complete a service request
    public boolean complete(int requestId) {

        String sql = "UPDATE ServiceRequests SET " +
                "status = 'COMPLETED', " +
                "completion_time = CURRENT_TIMESTAMP " +
                "WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error completing request: "
                    + e.getMessage());
        }

        return false;
    }

    // Add feedback
    public boolean addFeedback(int requestId, int rating,
                               String feedbackNotes) {

        String sql = "UPDATE ServiceRequests SET " +
                "feedback_rating = ?, " +
                "feedback_notes = ? " +
                "WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, rating);
            statement.setString(2, feedbackNotes);
            statement.setInt(3, requestId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding feedback: "
                    + e.getMessage());
        }

        return false;
    }

    // Delete request
    public boolean delete(int id) {

        String sql = "DELETE FROM ServiceRequests WHERE request_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting service request: "
                    + e.getMessage());
        }

        return false;
    }

    // Convert database row into ServiceRequest object
    private ServiceRequest mapResultSetToRequest(ResultSet rs)
            throws SQLException {

        Timestamp requestTime = rs.getTimestamp("request_time");
        Timestamp assignedTime = rs.getTimestamp("assigned_time");
        Timestamp completionTime = rs.getTimestamp("completion_time");

        return new ServiceRequest(
                rs.getInt("request_id"),
                rs.getInt("elderly_id"),
                rs.getString("service_type"),
                rs.getString("description"),
                rs.getString("urgency"),
                rs.getInt("assigned_caregiver_id"),
                rs.getString("status"),
                requestTime != null
                        ? requestTime.toLocalDateTime()
                        : null,
                assignedTime != null
                        ? assignedTime.toLocalDateTime()
                        : null,
                completionTime != null
                        ? completionTime.toLocalDateTime()
                        : null,
                rs.getInt("feedback_rating"),
                rs.getString("feedback_notes")
        );
    }
}

