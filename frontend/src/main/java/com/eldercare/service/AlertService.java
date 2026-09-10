package com.eldercare.service;

import com.eldercare.dao.AlertDAO;
import com.eldercare.models.Alert;

import java.util.List;

public class AlertService {

    private final AlertDAO alertDAO;

    public AlertService() {
        this.alertDAO = new AlertDAO();
    }

    // Add a new alert
    public boolean addAlert(Alert alert) {

        if (alert == null) {
            return false;
        }

        if (!alert.isValid()) {
            return false;
        }

        return alertDAO.add(alert);
    }

    // Find alert by ID
    public Alert getAlertById(int id) {

        if (id <= 0) {
            return null;
        }

        return alertDAO.findById(id);
    }

    // Get all alerts
    public List<Alert> getAllAlerts() {
        return alertDAO.findAll();
    }

    // Get alerts for a specific elderly person
    public List<Alert> getAlertsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return alertDAO.findByElderlyId(elderlyId);
    }

    // Get unacknowledged alerts
    public List<Alert> getUnacknowledgedAlerts() {
        return alertDAO.findUnacknowledged();
    }

    public boolean acknowledgeAlert(int alertId, int caregiverId) {

    if (alertId <= 0 || caregiverId <= 0) {
        return false;
    }

    return alertDAO.acknowledge(alertId, caregiverId);
}

    // Delete an alert
    public boolean deleteAlert(int alertId) {

        if (alertId <= 0) {
            return false;
        }

        return alertDAO.delete(alertId);
    }
}