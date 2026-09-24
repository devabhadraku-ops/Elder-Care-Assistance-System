package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // CREATE
    public boolean add(Appointment appointment) {

        String sql = """
                INSERT INTO appointments (
                    elderly_id,
                    appointment_type,
                    doctor_name,
                    clinic_name,
                    clinic_location,
                    clinic_phone,
                    appointment_date,
                    appointment_time,
                    duration_minutes,
                    status,
                    notes,
                    reminder_sent,
                    assigned_caregiver_id
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, appointment.getElderlyId());
            statement.setString(2, appointment.getAppointmentType());
            statement.setString(3, appointment.getDoctorName());
            statement.setString(4, appointment.getClinicName());
            statement.setString(5, appointment.getClinicLocation());
            statement.setString(6, appointment.getClinicPhone());
            statement.setDate(7, Date.valueOf(appointment.getAppointmentDate()));
            statement.setTime(8, Time.valueOf(appointment.getAppointmentTime()));
            statement.setInt(9, appointment.getDurationMinutes());
            statement.setString(10, appointment.getStatus());
            statement.setString(11, appointment.getNotes());
            statement.setBoolean(12, appointment.isReminderSent());

            if (appointment.getAssignedCaregiverId() > 0) {
                statement.setInt(13, appointment.getAssignedCaregiverId());
            } else {
                statement.setNull(13, Types.INTEGER);
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        appointment.setAppointmentId(keys.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
        }

        return false;
    }


    // READ BY ID
    public Appointment findById(int appointmentId) {

        String sql = """
                SELECT *
                FROM appointments
                WHERE appointment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToAppointment(resultSet);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding appointment: " + e.getMessage());
        }

        return null;
    }


    // GET ALL APPOINTMENTS FOR AN ELDERLY PERSON
    public List<Appointment> findByElderlyId(int elderlyId) {

        List<Appointment> appointments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM appointments
                WHERE elderly_id = ?
                ORDER BY appointment_date, appointment_time
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    appointments.add(mapResultSetToAppointment(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding elderly appointments: " + e.getMessage());
        }

        return appointments;
    }


    // GET UPCOMING APPOINTMENTS
    public List<Appointment> findUpcoming() {

        List<Appointment> appointments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM appointments
                WHERE status = 'SCHEDULED'
                  AND appointment_date >= CURDATE()
                ORDER BY appointment_date, appointment_time
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                appointments.add(mapResultSetToAppointment(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Error finding upcoming appointments: " + e.getMessage());
        }

        return appointments;
    }


    // GET APPOINTMENTS FOR A CAREGIVER
    public List<Appointment> findByCaregiverId(int caregiverId) {

        List<Appointment> appointments = new ArrayList<>();

        String sql = """
                SELECT *
                FROM appointments
                WHERE assigned_caregiver_id = ?
                ORDER BY appointment_date, appointment_time
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    appointments.add(mapResultSetToAppointment(resultSet));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding caregiver appointments: " + e.getMessage());
        }

        return appointments;
    }


    // UPDATE
    public boolean update(Appointment appointment) {

        String sql = """
                UPDATE appointments
                SET elderly_id = ?,
                    appointment_type = ?,
                    doctor_name = ?,
                    clinic_name = ?,
                    clinic_location = ?,
                    clinic_phone = ?,
                    appointment_date = ?,
                    appointment_time = ?,
                    duration_minutes = ?,
                    status = ?,
                    notes = ?,
                    reminder_sent = ?,
                    assigned_caregiver_id = ?
                WHERE appointment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointment.getElderlyId());
            statement.setString(2, appointment.getAppointmentType());
            statement.setString(3, appointment.getDoctorName());
            statement.setString(4, appointment.getClinicName());
            statement.setString(5, appointment.getClinicLocation());
            statement.setString(6, appointment.getClinicPhone());
            statement.setDate(7, Date.valueOf(appointment.getAppointmentDate()));
            statement.setTime(8, Time.valueOf(appointment.getAppointmentTime()));
            statement.setInt(9, appointment.getDurationMinutes());
            statement.setString(10, appointment.getStatus());
            statement.setString(11, appointment.getNotes());
            statement.setBoolean(12, appointment.isReminderSent());

            if (appointment.getAssignedCaregiverId() > 0) {
                statement.setInt(13, appointment.getAssignedCaregiverId());
            } else {
                statement.setNull(13, Types.INTEGER);
            }

            statement.setInt(14, appointment.getAppointmentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }

        return false;
    }


    // CANCEL APPOINTMENT
    public boolean cancel(int appointmentId) {

        String sql = """
                UPDATE appointments
                SET status = 'CANCELLED'
                WHERE appointment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error cancelling appointment: " + e.getMessage());
        }

        return false;
    }


    // MARK APPOINTMENT AS COMPLETED
    public boolean complete(int appointmentId) {

        String sql = """
                UPDATE appointments
                SET status = 'COMPLETED'
                WHERE appointment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error completing appointment: " + e.getMessage());
        }

        return false;
    }


    // MARK REMINDER AS SENT
    public boolean markReminderSent(int appointmentId) {

        String sql = """
                UPDATE appointments
                SET reminder_sent = TRUE
                WHERE appointment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error marking reminder as sent: " + e.getMessage());
        }

        return false;
    }


    // DELETE
    public boolean delete(int appointmentId) {

        String sql = """
                DELETE FROM appointments
                WHERE appointment_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, appointmentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }

        return false;
    }


    // MAP DATABASE ROW TO APPOINTMENT OBJECT
    private Appointment mapResultSetToAppointment(ResultSet resultSet)
            throws SQLException {

        int assignedCaregiverId = resultSet.getInt("assigned_caregiver_id");

        if (resultSet.wasNull()) {
            assignedCaregiverId = 0;
        }

        return new Appointment(
                resultSet.getInt("appointment_id"),
                resultSet.getInt("elderly_id"),
                resultSet.getString("appointment_type"),
                resultSet.getString("doctor_name"),
                resultSet.getString("clinic_name"),
                resultSet.getString("clinic_location"),
                resultSet.getString("clinic_phone"),
                resultSet.getDate("appointment_date").toLocalDate(),
                resultSet.getTime("appointment_time").toLocalTime(),
                resultSet.getInt("duration_minutes"),
                resultSet.getString("status"),
                resultSet.getString("notes"),
                resultSet.getBoolean("reminder_sent"),
                assignedCaregiverId
        );
    }
}
