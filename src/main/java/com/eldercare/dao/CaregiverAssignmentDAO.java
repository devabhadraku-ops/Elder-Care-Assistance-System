package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.CaregiverAssignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaregiverAssignmentDAO {

    public boolean add(CaregiverAssignment assignment) {

        String sql = "INSERT INTO caregiverassignments " +
                "(caregiver_id, elderly_id, start_date, end_date, " +
                "assignment_reason, assignment_status, hours_per_week) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, assignment.getCaregiverId());
            statement.setInt(2, assignment.getElderlyId());
            statement.setDate(3, Date.valueOf(assignment.getStartDate()));

            if (assignment.getEndDate() != null) {
                statement.setDate(4, Date.valueOf(assignment.getEndDate()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, assignment.getAssignmentReason());
            statement.setString(6, assignment.getAssignmentStatus());
            statement.setInt(7, assignment.getHoursPerWeek());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        assignment.setAssignmentId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding caregiver assignment: " + e.getMessage());
        }

        return false;
    }

    public CaregiverAssignment findById(int id) {

        String sql = "SELECT * FROM caregiverassignments WHERE assignment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAssignment(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding caregiver assignment: " + e.getMessage());
        }

        return null;
    }

    public List<CaregiverAssignment> findByCaregiverId(int caregiverId) {

        String sql = "SELECT * FROM caregiverassignments " +
                "WHERE caregiver_id = ? ORDER BY start_date DESC";

        List<CaregiverAssignment> assignments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding caregiver assignments: " + e.getMessage());
        }

        return assignments;
    }

    public List<CaregiverAssignment> findByElderlyId(int elderlyId) {

        String sql = "SELECT * FROM caregiverassignments " +
                "WHERE elderly_id = ? ORDER BY start_date DESC";

        List<CaregiverAssignment> assignments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapResultSetToAssignment(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly assignments: " + e.getMessage());
        }

        return assignments;
    }

    public List<CaregiverAssignment> findAllActive() {

        String sql = "SELECT * FROM caregiverassignments " +
                "WHERE assignment_status = 'ACTIVE' " +
                "ORDER BY start_date DESC";

        List<CaregiverAssignment> assignments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                assignments.add(mapResultSetToAssignment(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding active assignments: " + e.getMessage());
        }

        return assignments;
    }

    public boolean update(CaregiverAssignment assignment) {

        String sql = "UPDATE caregiverassignments SET " +
                "caregiver_id = ?, elderly_id = ?, start_date = ?, " +
                "end_date = ?, assignment_reason = ?, " +
                "assignment_status = ?, hours_per_week = ? " +
                "WHERE assignment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, assignment.getCaregiverId());
            statement.setInt(2, assignment.getElderlyId());
            statement.setDate(3, Date.valueOf(assignment.getStartDate()));

            if (assignment.getEndDate() != null) {
                statement.setDate(4, Date.valueOf(assignment.getEndDate()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, assignment.getAssignmentReason());
            statement.setString(6, assignment.getAssignmentStatus());
            statement.setInt(7, assignment.getHoursPerWeek());
            statement.setInt(8, assignment.getAssignmentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating caregiver assignment: " + e.getMessage());
        }

        return false;
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM caregiverassignments WHERE assignment_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting caregiver assignment: " + e.getMessage());
        }

        return false;
    }

    private CaregiverAssignment mapResultSetToAssignment(ResultSet rs)
            throws SQLException {

        Date startDate = rs.getDate("start_date");
        Date endDate = rs.getDate("end_date");

        return new CaregiverAssignment(
                rs.getInt("assignment_id"),
                rs.getInt("caregiver_id"),
                rs.getInt("elderly_id"),
                startDate != null ? startDate.toLocalDate() : null,
                endDate != null ? endDate.toLocalDate() : null,
                rs.getString("assignment_reason"),
                rs.getString("assignment_status"),
                rs.getInt("hours_per_week")
        );
    }
}