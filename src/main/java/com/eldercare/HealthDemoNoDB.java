package com.eldercare;

import com.eldercare.models.*;
import com.eldercare.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Health alert demo that needs NO database. The database-backed services are
 * replaced by small in-memory versions, but HealthMonitor and HealthAlert
 * are the real classes.
 */
public class HealthDemoNoDB {

    public static void main(String[] args) {

        // ----- test data kept in memory -----
        Elderly elder = new Elderly("Ramesh Kumar", 72, "9000000001",
                LocalDate.of(1954, 3, 10));
        elder.setElderlyId(1);
        elder.setEmergencyContact1Phone("8000000001");
        elder.setEmergencyContact2Phone("7000000001");

        Caregiver caregiver = new Caregiver("Anitha Nurse", "6000000001");
        caregiver.setCaregiverId(1);

        List<HealthMetrics> savedReadings = new ArrayList<>();
        List<Alert> savedAlerts = new ArrayList<>();

        // ----- in-memory replacements for the database services -----
        HealthMetricsService metricsService = new HealthMetricsService() {
            @Override
            public boolean addMetrics(HealthMetrics metrics) {
                metrics.setMetricsId(savedReadings.size() + 1);
                savedReadings.add(metrics);
                return true;
            }

            @Override
            public boolean linkAlert(int metricsId, int alertId) {
                savedReadings.get(metricsId - 1).setAlertGenerated(true);
                savedReadings.get(metricsId - 1).setAlertId(alertId);
                return true;
            }
        };

        ElderlyService elderlyService = new ElderlyService() {
            @Override
            public Elderly getElderlyById(int id) {
                return id == 1 ? elder : null;
            }
        };

        CaregiverService caregiverService = new CaregiverService() {
            @Override
            public List<Caregiver> getAvailableCaregivers() {
                return List.of(caregiver);
            }
        };

        AlertService alertService = new AlertService() {
            @Override
            public boolean addAlert(Alert alert) {
                alert.setAlertId(savedAlerts.size() + 1);
                savedAlerts.add(alert);
                return true;
            }
        };

        HealthMonitor monitor = new HealthMonitor(metricsService, elderlyService,
                caregiverService, alertService, new NotificationService());

        // ----- three readings -----
        System.out.println("=== 1. Normal reading ===");
        monitor.recordReading(new HealthMetrics(1, 120, 80, 72, 36.8, 98, 68.0, "Ramesh"));

        System.out.println("\n=== 2. Slightly high blood pressure ===");
        monitor.recordReading(new HealthMetrics(1, 150, 95, 78, 36.9, 97, 68.0, "Ramesh"));

        System.out.println("\n=== 3. Dangerous reading ===");
        monitor.recordReading(new HealthMetrics(1, 185, 125, 115, 39.2, 88, 68.0, "Ramesh"));

        // ----- summary -----
        System.out.println("\n--- Summary ---");
        System.out.println("Readings saved: " + savedReadings.size());
        System.out.println("Alerts raised: " + savedAlerts.size());
        for (Alert a : savedAlerts) {
            System.out.println("  " + a.getSeverity() + " -> " + a.getAlertMessage());
        }

        // Same list type holds different alert kinds: polymorphism
        Alert first = savedAlerts.get(0);
        System.out.println("\nFirst alert is a HealthAlert: " + (first instanceof HealthAlert));
    }
}
