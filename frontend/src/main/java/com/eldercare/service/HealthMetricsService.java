package com.eldercare.service;

import com.eldercare.dao.HealthMetricsDAO;
import com.eldercare.models.HealthMetrics;

import java.util.List;

public class HealthMetricsService {

    private final HealthMetricsDAO healthMetricsDAO;

    public HealthMetricsService() {
        this.healthMetricsDAO = new HealthMetricsDAO();
    }

    // Add health metrics
    public boolean addMetrics(HealthMetrics metrics) {

        if (metrics == null) {
            return false;
        }

        if (!metrics.isValid()) {
            return false;
        }

        return healthMetricsDAO.add(metrics);
    }

    // Find metrics by ID
    public HealthMetrics getMetricsById(int id) {

        if (id <= 0) {
            return null;
        }

        return healthMetricsDAO.findById(id);
    }

    // Get metrics for an elderly person
    public List<HealthMetrics> getMetricsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return healthMetricsDAO.findByElderlyId(elderlyId);
    }

    // Get latest health metrics
    public HealthMetrics getLatestMetrics(int elderlyId) {

        if (elderlyId <= 0) {
            return null;
        }

        return healthMetricsDAO.findLatest(elderlyId);
    }

    // Get metrics where an alert was generated
    public List<HealthMetrics> getAlertGeneratedMetrics() {
        return healthMetricsDAO.findAlertGenerated();
    }

    // Link health metrics to an alert
    public boolean linkAlert(int metricsId, int alertId) {

        if (metricsId <= 0 || alertId <= 0) {
            return false;
        }

        return healthMetricsDAO.linkAlert(metricsId, alertId);
    }

    // Delete health metrics
    public boolean deleteMetrics(int metricsId) {

        if (metricsId <= 0) {
            return false;
        }

        return healthMetricsDAO.delete(metricsId);
    }
}
