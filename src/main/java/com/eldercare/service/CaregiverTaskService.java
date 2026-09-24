package com.eldercare.service;

import com.eldercare.dao.CaregiverTaskDAO;
import com.eldercare.models.CaregiverTask;

import java.util.List;

public class CaregiverTaskService {

    private final CaregiverTaskDAO taskDAO;

    public CaregiverTaskService() {
        this.taskDAO = new CaregiverTaskDAO();
    }

    public boolean addTask(CaregiverTask task) {

        if (task == null || !task.isValid()) {
            return false;
        }

        return taskDAO.add(task);
    }

    public CaregiverTask getTaskById(int taskId) {

        if (taskId <= 0) {
            return null;
        }

        return taskDAO.findById(taskId);
    }

    public List<CaregiverTask> getTasksByCaregiverId(int caregiverId) {

        if (caregiverId <= 0) {
            return List.of();
        }

        return taskDAO.findByCaregiverId(caregiverId);
    }

    public List<CaregiverTask> getTasksByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return taskDAO.findByElderlyId(elderlyId);
    }

    public List<CaregiverTask> getPendingTasks() {

        return taskDAO.findPending();
    }

    public boolean completeTask(int taskId) {

        if (taskId <= 0) {
            return false;
        }

        return taskDAO.complete(taskId);
    }

    public boolean updateTask(CaregiverTask task) {

        if (task == null
                || task.getTaskId() <= 0
                || !task.isValid()) {
            return false;
        }

        return taskDAO.update(task);
    }

    public boolean deleteTask(int taskId) {

        if (taskId <= 0) {
            return false;
        }

        return taskDAO.delete(taskId);
    }
}