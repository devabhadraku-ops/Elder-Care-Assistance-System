package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.Medication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

    // CREATE
    public boolean add(Medication medication) {

        String sql = """
                INSERT INTO medications (
                    elderly_id,
                    medicine_name,
                    dosage,
                    frequency,
                    start_date,
                    end_date,
                    prescribed_by,
                    prescription_date,
                    notes,
                    is_active
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, medication.getElderlyId());
            statement.setString(2, medication.getMedicineName());
            statement.setString(3, medication.getDosage());
            statement.setString(4, medication.getFrequency());

            if (medication.getStartDate() != null) {
                statement.setDate(5, Date.valueOf(medication.getStartDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            if (medication.getEndDate() != null) {
                statement.setDate(6, Date.valueOf(medication.getEndDate()));
            } else {
                statement.setNull(6, Types.DATE);
            }

            statement.setString(7, medication.getPrescribedBy());

            if (medication.getPrescriptionDate() != null) {
                statement.setDate(
                        8,
                        Date.valueOf(medication.getPrescriptionDate())
                );
            } else {
                statement.setNull(8, Types.DATE);
            }

            statement.setString(9, medication.getNotes());
            statement.setBoolean(10, medication.isActive());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        medication.setMedicationId(keys.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding medication: " + e.getMessage());
        }

        return false;
    }


    // READ BY ID
    public Medication findById(int medicationId) {

        String sql = """
                SELECT *
                FROM medications
                WHERE medication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, medicationId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToMedication(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding medication: " + e.getMessage());
        }

        return null;
    }


    // GET ALL MEDICATIONS FOR AN ELDERLY PERSON
    public List<Medication> findByElderlyId(int elderlyId) {

        List<Medication> medications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM medications
                WHERE elderly_id = ?
                ORDER BY medicine_name
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    medications.add(mapResultSetToMedication(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly medications: "
                    + e.getMessage());
        }

        return medications;
    }


    // GET ACTIVE MEDICATIONS FOR AN ELDERLY PERSON
    public List<Medication> findActiveByElderlyId(int elderlyId) {

        List<Medication> medications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM medications
                WHERE elderly_id = ?
                  AND is_active = TRUE
                ORDER BY medicine_name
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    medications.add(mapResultSetToMedication(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding active medications: "
                    + e.getMessage());
        }

        return medications;
    }


    // GET ALL ACTIVE MEDICATIONS
    public List<Medication> findAllActive() {

        List<Medication> medications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM medications
                WHERE is_active = TRUE
                ORDER BY elderly_id, medicine_name
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                medications.add(mapResultSetToMedication(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Error finding active medications: "
                    + e.getMessage());
        }

        return medications;
    }


    // UPDATE
    public boolean update(Medication medication) {

        String sql = """
                UPDATE medications
                SET elderly_id = ?,
                    medicine_name = ?,
                    dosage = ?,
                    frequency = ?,
                    start_date = ?,
                    end_date = ?,
                    prescribed_by = ?,
                    prescription_date = ?,
                    notes = ?,
                    is_active = ?
                WHERE medication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, medication.getElderlyId());
            statement.setString(2, medication.getMedicineName());
            statement.setString(3, medication.getDosage());
            statement.setString(4, medication.getFrequency());

            if (medication.getStartDate() != null) {
                statement.setDate(5, Date.valueOf(medication.getStartDate()));
            } else {
                statement.setNull(5, Types.DATE);
            }

            if (medication.getEndDate() != null) {
                statement.setDate(6, Date.valueOf(medication.getEndDate()));
            } else {
                statement.setNull(6, Types.DATE);
            }

            statement.setString(7, medication.getPrescribedBy());

            if (medication.getPrescriptionDate() != null) {
                statement.setDate(
                        8,
                        Date.valueOf(medication.getPrescriptionDate())
                );
            } else {
                statement.setNull(8, Types.DATE);
            }

            statement.setString(9, medication.getNotes());
            statement.setBoolean(10, medication.isActive());
            statement.setInt(11, medication.getMedicationId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating medication: "
                    + e.getMessage());
        }

        return false;
    }


    // DEACTIVATE MEDICATION
    public boolean deactivate(int medicationId) {

        String sql = """
                UPDATE medications
                SET is_active = FALSE
                WHERE medication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, medicationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deactivating medication: "
                    + e.getMessage());
        }

        return false;
    }


    // ACTIVATE MEDICATION
    public boolean activate(int medicationId) {

        String sql = """
                UPDATE medications
                SET is_active = TRUE
                WHERE medication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, medicationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error activating medication: "
                    + e.getMessage());
        }

        return false;
    }


    // DELETE
    public boolean delete(int medicationId) {

        String sql = """
                DELETE FROM medications
                WHERE medication_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, medicationId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting medication: "
                    + e.getMessage());
        }

        return false;
    }


    // MAP DATABASE ROW TO MEDICATION OBJECT
    private Medication mapResultSetToMedication(ResultSet resultSet)
            throws SQLException {

        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        Date prescriptionDate = resultSet.getDate("prescription_date");

        Timestamp createdTimestamp =
                resultSet.getTimestamp("created_date");

        return new Medication(
                resultSet.getInt("medication_id"),
                resultSet.getInt("elderly_id"),
                resultSet.getString("medicine_name"),
                resultSet.getString("dosage"),
                resultSet.getString("frequency"),
                startDate != null ? startDate.toLocalDate() : null,
                endDate != null ? endDate.toLocalDate() : null,
                resultSet.getString("prescribed_by"),
                prescriptionDate != null
                        ? prescriptionDate.toLocalDate()
                        : null,
                resultSet.getString("notes"),
                resultSet.getBoolean("is_active"),
                createdTimestamp != null
                        ? createdTimestamp.toLocalDateTime()
                        : null
        );
    }
}