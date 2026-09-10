package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.Caregiver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaregiverDAO {

    // Add a new caregiver
    public boolean add(Caregiver caregiver) {

        String sql = "INSERT INTO Caregivers " +
                "(name, phone, email, address, qualification, " +
                "experience_years, specialization, availability_status, " +
                "preferred_shift, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, caregiver.getName());
            statement.setString(2, caregiver.getPhone());
            statement.setString(3, caregiver.getEmail());
            statement.setString(4, caregiver.getAddress());
            statement.setString(5, caregiver.getQualification());
            statement.setInt(6, caregiver.getExperienceYears());
            statement.setString(7, caregiver.getSpecialization());
            statement.setString(8, caregiver.getAvailabilityStatus());
            statement.setString(9, caregiver.getPreferredShift());
            statement.setString(10, caregiver.getNotes());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        caregiver.setCaregiverId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding caregiver: " + e.getMessage());
        }

        return false;
    }

    // Find caregiver by ID
    public Caregiver findById(int id) {

        String sql = "SELECT * FROM Caregivers WHERE caregiver_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCaregiver(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding caregiver: " + e.getMessage());
        }

        return null;
    }

    // Find caregivers by name
    public List<Caregiver> findByName(String name) {

        List<Caregiver> caregivers = new ArrayList<>();

        String sql = "SELECT * FROM Caregivers WHERE name LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + name + "%");

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    caregivers.add(mapResultSetToCaregiver(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding caregiver by name: "
                    + e.getMessage());
        }

        return caregivers;
    }

    // Find all caregivers
    public List<Caregiver> findAll() {

        List<Caregiver> caregivers = new ArrayList<>();

        String sql = "SELECT * FROM Caregivers ORDER BY caregiver_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                caregivers.add(mapResultSetToCaregiver(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding all caregivers: "
                    + e.getMessage());
        }

        return caregivers;
    }

    // Find available caregivers
    public List<Caregiver> findAvailable() {

        List<Caregiver> caregivers = new ArrayList<>();

        String sql = "SELECT * FROM Caregivers " +
                "WHERE availability_status = 'AVAILABLE' " +
                "AND is_active = TRUE " +
                "ORDER BY caregiver_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                caregivers.add(mapResultSetToCaregiver(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding available caregivers: "
                    + e.getMessage());
        }

        return caregivers;
    }

    // Update caregiver
    public boolean update(Caregiver caregiver) {

        String sql = "UPDATE Caregivers SET " +
                "name = ?, phone = ?, email = ?, address = ?, " +
                "qualification = ?, experience_years = ?, " +
                "specialization = ?, availability_status = ?, " +
                "preferred_shift = ?, is_active = ?, notes = ? " +
                "WHERE caregiver_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, caregiver.getName());
            statement.setString(2, caregiver.getPhone());
            statement.setString(3, caregiver.getEmail());
            statement.setString(4, caregiver.getAddress());
            statement.setString(5, caregiver.getQualification());
            statement.setInt(6, caregiver.getExperienceYears());
            statement.setString(7, caregiver.getSpecialization());
            statement.setString(8, caregiver.getAvailabilityStatus());
            statement.setString(9, caregiver.getPreferredShift());
            statement.setBoolean(10, caregiver.isActive());
            statement.setString(11, caregiver.getNotes());
            statement.setInt(12, caregiver.getCaregiverId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating caregiver: "
                    + e.getMessage());
        }

        return false;
    }

    // Delete caregiver
    public boolean delete(int id) {

        String sql = "DELETE FROM Caregivers WHERE caregiver_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting caregiver: "
                    + e.getMessage());
        }

        return false;
    }

    // Convert database row into Caregiver object
    private Caregiver mapResultSetToCaregiver(ResultSet rs)
            throws SQLException {

        Timestamp registrationTimestamp =
                rs.getTimestamp("registration_date");

        return new Caregiver(
                rs.getInt("caregiver_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("qualification"),
                rs.getInt("experience_years"),
                rs.getString("specialization"),
                rs.getString("availability_status"),
                rs.getString("preferred_shift"),
                registrationTimestamp != null
                        ? registrationTimestamp.toLocalDateTime()
                        : null,
                rs.getBoolean("is_active"),
                rs.getString("notes")
        );
    }
}