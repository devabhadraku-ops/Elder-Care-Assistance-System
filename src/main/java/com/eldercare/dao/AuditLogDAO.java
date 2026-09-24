package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    public boolean add(AuditLog auditLog) {

        String sql = """
                INSERT INTO auditlog
                (table_name, record_id, action, old_values, new_values, changed_by)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, auditLog.getTableName());
            statement.setInt(2, auditLog.getRecordId());
            statement.setString(3, auditLog.getAction());
            statement.setString(4, auditLog.getOldValues());
            statement.setString(5, auditLog.getNewValues());
            statement.setString(6, auditLog.getChangedBy());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    auditLog.setLogId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public AuditLog findById(int logId) {

        String sql = """
                SELECT *
                FROM auditlog
                WHERE log_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, logId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToAuditLog(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<AuditLog> findByTableName(String tableName) {

        String sql = """
                SELECT *
                FROM auditlog
                WHERE table_name = ?
                ORDER BY changed_time DESC
                """;

        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, tableName);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    auditLogs.add(mapResultSetToAuditLog(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return auditLogs;
    }


    public List<AuditLog> findByRecordId(int recordId) {

        String sql = """
                SELECT *
                FROM auditlog
                WHERE record_id = ?
                ORDER BY changed_time DESC
                """;

        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, recordId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    auditLogs.add(mapResultSetToAuditLog(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return auditLogs;
    }


    public List<AuditLog> findAll() {

        String sql = """
                SELECT *
                FROM auditlog
                ORDER BY changed_time DESC
                """;

        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                auditLogs.add(mapResultSetToAuditLog(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return auditLogs;
    }


    public boolean delete(int logId) {

        String sql = """
                DELETE FROM auditlog
                WHERE log_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, logId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private AuditLog mapResultSetToAuditLog(ResultSet resultSet)
            throws SQLException {

        Timestamp changedTime = resultSet.getTimestamp("changed_time");

        return new AuditLog(
                resultSet.getInt("log_id"),
                resultSet.getString("table_name"),
                resultSet.getInt("record_id"),
                resultSet.getString("action"),
                resultSet.getString("old_values"),
                resultSet.getString("new_values"),
                resultSet.getString("changed_by"),
                changedTime != null ? changedTime.toLocalDateTime() : null
        );
    }
}
