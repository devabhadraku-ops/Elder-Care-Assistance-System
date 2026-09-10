package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.Elderly;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ElderlyDAO {

    // Add a new elderly person
    public boolean add(Elderly elderly) {

        String sql = "INSERT INTO Elderly " +
                "(name, age, phone, email, address, date_of_birth, " +
                "blood_type, medical_conditions, allergies, current_medications, " +
                "emergency_contact_1_name, emergency_contact_1_phone, " +
                "emergency_contact_2_name, emergency_contact_2_phone, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, elderly.getName());
            statement.setInt(2, elderly.getAge());
            statement.setString(3, elderly.getPhone());
            statement.setString(4, elderly.getEmail());
            statement.setString(5, elderly.getAddress());

            if (elderly.getDateOfBirth() != null) {
                statement.setDate(6, Date.valueOf(elderly.getDateOfBirth()));
            } else {
                statement.setNull(6, Types.DATE);
            }

            statement.setString(7, elderly.getBloodType());
            statement.setString(8, elderly.getMedicalConditions());
            statement.setString(9, elderly.getAllergies());
            statement.setString(10, elderly.getCurrentMedications());
            statement.setString(11, elderly.getEmergencyContact1Name());
            statement.setString(12, elderly.getEmergencyContact1Phone());
            statement.setString(13, elderly.getEmergencyContact2Name());
            statement.setString(14, elderly.getEmergencyContact2Phone());
            statement.setString(15, elderly.getNotes());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        elderly.setElderlyId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding elderly: " + e.getMessage());
        }

        return false;
    }

    // Find elderly person by ID
    public Elderly findById(int id) {

        String sql = "SELECT * FROM Elderly WHERE elderly_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToElderly(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly: " + e.getMessage());
        }

        return null;
    }

    // Find elderly people by name
    public List<Elderly> findByName(String name) {

        List<Elderly> elderlyList = new ArrayList<>();

        String sql = "SELECT * FROM Elderly WHERE name LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + name + "%");

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    elderlyList.add(mapResultSetToElderly(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly by name: " + e.getMessage());
        }

        return elderlyList;
    }

    // Find all elderly people
    public List<Elderly> findAll() {

        List<Elderly> elderlyList = new ArrayList<>();

        String sql = "SELECT * FROM Elderly ORDER BY elderly_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                elderlyList.add(mapResultSetToElderly(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error finding all elderly: " + e.getMessage());
        }

        return elderlyList;
    }

    // Update elderly person
    public boolean update(Elderly elderly) {

        String sql = "UPDATE Elderly SET " +
                "name = ?, age = ?, phone = ?, email = ?, address = ?, " +
                "date_of_birth = ?, blood_type = ?, medical_conditions = ?, " +
                "allergies = ?, current_medications = ?, " +
                "emergency_contact_1_name = ?, emergency_contact_1_phone = ?, " +
                "emergency_contact_2_name = ?, emergency_contact_2_phone = ?, " +
                "is_active = ?, notes = ? " +
                "WHERE elderly_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, elderly.getName());
            statement.setInt(2, elderly.getAge());
            statement.setString(3, elderly.getPhone());
            statement.setString(4, elderly.getEmail());
            statement.setString(5, elderly.getAddress());

            if (elderly.getDateOfBirth() != null) {
                statement.setDate(6, Date.valueOf(elderly.getDateOfBirth()));
            } else {
                statement.setNull(6, Types.DATE);
            }

            statement.setString(7, elderly.getBloodType());
            statement.setString(8, elderly.getMedicalConditions());
            statement.setString(9, elderly.getAllergies());
            statement.setString(10, elderly.getCurrentMedications());
            statement.setString(11, elderly.getEmergencyContact1Name());
            statement.setString(12, elderly.getEmergencyContact1Phone());
            statement.setString(13, elderly.getEmergencyContact2Name());
            statement.setString(14, elderly.getEmergencyContact2Phone());
            statement.setBoolean(15, elderly.isActive());
            statement.setString(16, elderly.getNotes());
            statement.setInt(17, elderly.getElderlyId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating elderly: " + e.getMessage());
        }

        return false;
    }

    // Delete elderly person
    public boolean delete(int id) {

        String sql = "DELETE FROM Elderly WHERE elderly_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting elderly: " + e.getMessage());
        }

        return false;
    }

    // Convert database row into Elderly object
    private Elderly mapResultSetToElderly(ResultSet rs) throws SQLException {

        Date dob = rs.getDate("date_of_birth");

        LocalDate dateOfBirth = dob != null
                ? dob.toLocalDate()
                : null;

        Timestamp registrationTimestamp =
                rs.getTimestamp("registration_date");

        return new Elderly(
                rs.getInt("elderly_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                dateOfBirth,
                rs.getString("blood_type"),
                rs.getString("medical_conditions"),
                rs.getString("allergies"),
                rs.getString("current_medications"),
                rs.getString("emergency_contact_1_name"),
                rs.getString("emergency_contact_1_phone"),
                rs.getString("emergency_contact_2_name"),
                rs.getString("emergency_contact_2_phone"),
                registrationTimestamp != null
                        ? registrationTimestamp.toLocalDateTime()
                        : null,
                rs.getBoolean("is_active"),
                rs.getString("notes")
        );
    }
}