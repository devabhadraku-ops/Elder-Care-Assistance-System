package com.eldercare;

import com.eldercare.models.Alert;
import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.models.SOSAlert;
import com.eldercare.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SOS demo that needs NO database. The database-backed services are
 * replaced by small in-memory versions, but EmergencyService, SOSAlert
 * and NotificationService are the real classes.
 */
public class SOSDemoNoDB {

    public static void main(String[] args) {

        // ----- test data kept in memory -----
        Elderly elder = new Elderly("Ramesh Kumar", 72, "9000000001",
                LocalDate.of(1954, 3, 10));
        elder.setElderlyId(1);
        elder.setEmergencyContact1Name("Suresh (son)");
        elder.setEmergencyContact1Phone("8000000001");
        elder.setEmergencyContact2Name("Latha (daughter)");
        elder.setEmergencyContact2Phone("7000000001");

        Caregiver caregiver = new Caregiver("Anitha Nurse", "6000000001");
        caregiver.setCaregiverId(1);

        List<Alert> savedAlerts = new ArrayList<>();

        // ----- in-memory replacements for the database services -----
        ElderlyService elderlyService = new ElderlyService() {
            @Override
            public Elderly getElderlyById(int id) {
                return id == elder.getElderlyId() ? elder : null;
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

            @Override
            public List<Alert> getUnacknowledgedAlerts() {
                List<Alert> result = new ArrayList<>();
                for (Alert a : savedAlerts) {
                    if (!a.isAcknowledged()) {
                        result.add(a);
                    }
                }
                return result;
            }

            @Override
            public boolean acknowledgeAlert(int alertId, int caregiverId) {
                for (Alert a : savedAlerts) {
                    if (a.getAlertId() == alertId) {
                        a.acknowledge(caregiverId);
                        return true;
                    }
                }
                return false;
            }
        };

        EmergencyService emergencyService = new EmergencyService(
                elderlyService, caregiverService, alertService,
                new NotificationService());

        // ----- 1. Elderly person presses the SOS button -----
        System.out.println("Ramesh presses the SOS button...");
        SOSAlert alert = emergencyService.triggerSOS(1, "Fall detected");

        // ----- 2. Caregiver dashboard -----
        System.out.println("\n--- Caregiver dashboard: active alerts ---");
        for (Alert a : emergencyService.getActiveAlerts()) {
            System.out.println(a);
        }

        // ----- 3. Caregiver acknowledges -----
        System.out.println("\nCaregiver " + caregiver.getName()
                + " acknowledges alert " + alert.getAlertId() + "...");
        emergencyService.acknowledge(alert.getAlertId(), caregiver.getCaregiverId());

        System.out.println("\n--- Caregiver dashboard after acknowledging ---");
        System.out.println("Active alerts: " + emergencyService.getActiveAlerts().size());
        System.out.println("Acknowledged by caregiver id: "
                + savedAlerts.get(0).getAcknowledgmentBy());
    }
}
