package com.eldercare.dao;

import com.eldercare.config.DatabaseConnection;
import com.eldercare.models.CaregiverTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaregiverTaskDAO {

    // Add a new task
    public boolean add(CaregiverTask task) {

        String sql = """
                INSERT INTO caregivertasks
                (caregiver_id, elderly_id, task_type, description, due_date, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, task.getCaregiverId());
            statement.setInt(2, task.getElderlyId());
            statement.setString(3, task.getTaskType());
            statement.setString(4, task.getDescription());

            if (task.getDueDate() != null) {
                statement.setTimestamp(5, Timestamp.valueOf(task.getDueDate()));
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }

            statement.setString(6, task.getStatus());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    task.setTaskId(keys.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Find task by ID
    public CaregiverTask findById(int taskId) {

        String sql = """
                SELECT *
                FROM caregivertasks
                WHERE task_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToTask(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Find tasks assigned to a caregiver
    public List<CaregiverTask> findByCaregiverId(int caregiverId) {

        List<CaregiverTask> tasks = new ArrayList<>();

        String sql = """
                SELECT *
                FROM caregivertasks
                WHERE caregiver_id = ?
                ORDER BY due_date
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caregiverId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    tasks.add(mapResultSetToTask(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }


    // Find tasks for an elderly person
    public List<CaregiverTask> findByElderlyId(int elderlyId) {

        List<CaregiverTask> tasks = new ArrayList<>();

        String sql = """
                SELECT *
                FROM caregivertasks
                WHERE elderly_id = ?
                ORDER BY due_date
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, elderlyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    tasks.add(mapResultSetToTask(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }


    // Find pending tasks
    public List<CaregiverTask> findPending() {

        List<CaregiverTask> tasks = new ArrayList<>();

        String sql = """
                SELECT *
                FROM caregivertasks
                WHERE status = 'PENDING'
                ORDER BY due_date
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tasks.add(mapResultSetToTask(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }


    // Mark task as completed
    public boolean complete(int taskId) {

        String sql = """
                UPDATE caregivertasks
                SET status = 'COMPLETED',
                    completion_time = CURRENT_TIMESTAMP
                WHERE task_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Update task
    public boolean update(CaregiverTask task) {

        String sql = """
                UPDATE caregivertasks
                SET caregiver_id = ?,
                    elderly_id = ?,
                    task_type = ?,
                    description = ?,
                    due_date = ?,
                    status = ?,
                    completion_time = ?
                WHERE task_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, task.getCaregiverId());
            statement.setInt(2, task.getElderlyId());
            statement.setString(3, task.getTaskType());
            statement.setString(4, task.getDescription());

            if (task.getDueDate() != null) {
                statement.setTimestamp(5, Timestamp.valueOf(task.getDueDate()));
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }

            statement.setString(6, task.getStatus());

            if (task.getCompletionTime() != null) {
                statement.setTimestamp(
                        7,
                        Timestamp.valueOf(task.getCompletionTime())
                );
            } else {
                statement.setNull(7, Types.TIMESTAMP);
            }

            statement.setInt(8, task.getTaskId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Delete task
    public boolean delete(int taskId) {

        String sql = """
                DELETE FROM caregivertasks
                WHERE task_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, taskId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Convert database row into CaregiverTask object
    private CaregiverTask mapResultSetToTask(ResultSet resultSet)
            throws SQLException {

        Timestamp createdTimestamp =
                resultSet.getTimestamp("created_date");

        Timestamp dueTimestamp =
                resultSet.getTimestamp("due_date");

        Timestamp completionTimestamp =
                resultSet.getTimestamp("completion_time");

        return new CaregiverTask(
                resultSet.getInt("task_id"),
                resultSet.getInt("caregiver_id"),
                resultSet.getInt("elderly_id"),
                resultSet.getString("task_type"),
                resultSet.getString("description"),
                createdTimestamp != null
                        ? createdTimestamp.toLocalDateTime()
                        : null,
                dueTimestamp != null
                        ? dueTimestamp.toLocalDateTime()
                        : null,
                resultSet.getString("status"),
                completionTimestamp != null
                        ? completionTimestamp.toLocalDateTime()
                        : null
        );
    }
}