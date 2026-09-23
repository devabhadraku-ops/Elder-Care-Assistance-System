package com.eldercare.service;

import com.eldercare.models.Alert;
import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.models.SOSAlert;

import java.util.List;

/**
 * Handles the whole SOS flow:
 *   1. find the elderly person
 *   2. build an SOSAlert
 *   3. notify emergency contacts + caregivers  (FIRST, so a database
 *      problem can never block a real emergency)
 *   4. save the alert in the database
 *
 * The UI only needs to call triggerSOS(...).
 */
public class EmergencyService {

    private final ElderlyService elderlyService;
    private final CaregiverService caregiverService;
    private final AlertService alertService;
    private final NotificationService notificationService;

    public EmergencyService() {
        this(new ElderlyService(), new CaregiverService(),
                new AlertService(), new NotificationService());
    }

    // Constructor that lets tests pass in their own services
    public EmergencyService(ElderlyService elderlyService,
                            CaregiverService caregiverService,
                            AlertService alertService,
                            NotificationService notificationService) {
        this.elderlyService = elderlyService;
        this.caregiverService = caregiverService;
        this.alertService = alertService;
        this.notificationService = notificationService;
    }

    /**
     * Called when the SOS button is pressed.
     * @return the SOSAlert that was created, or null if the elderly
     *         person could not be found.
     */
    public SOSAlert triggerSOS(int elderlyId, String reason) {

        System.out.println("\n=== SOS TRIGGERED for elderly #" + elderlyId + " ===");

        Elderly elder = elderlyService.getElderlyById(elderlyId);
        if (elder == null) {
            System.out.println("ERROR: elderly person not found - SOS not sent");
            return null;
        }

        SOSAlert alert = new SOSAlert(elderlyId, elder.getName(), reason);
        for (String contact : elder.getEmergencyContacts()) {
            alert.addEmergencyContact(contact);
        }

        if (alert.getEmergencyContacts().isEmpty()) {
            System.out.println("WARNING: no emergency contacts on file for "
                    + elder.getName());
        }

        // Step 1: notify people first
        notifyEmergencyContacts(alert);
        notifyCaregivers(alert);

        // Step 2: save afterwards
        if (alertService.addAlert(alert)) {
            System.out.println("Alert saved with id " + alert.getAlertId());
        } else {
            System.out.println("WARNING: people were notified, but the alert "
                    + "could not be saved to the database");
        }

        return alert;
    }

    /** Alerts still waiting for a caregiver (for the caregiver dashboard). */
    public List<Alert> getActiveAlerts() {
        return alertService.getUnacknowledgedAlerts();
    }

    /** A caregiver confirms they have seen the alert. */
    public boolean acknowledge(int alertId, int caregiverId) {
        return alertService.acknowledgeAlert(alertId, caregiverId);
    }

    // ---------------------------------------------------------------

    private void notifyEmergencyContacts(SOSAlert alert) {
        for (String phone : alert.getEmergencyContacts()) {
            try {
                notificationService.sendSMS(phone, alert.getAlertMessage());
                notificationService.sendCall(phone, "Automated emergency call");
            } catch (Exception e) {
                System.out.println("Could not notify " + phone + ": " + e.getMessage());
            }
        }
    }

    private void notifyCaregivers(SOSAlert alert) {
        try {
            List<Caregiver> caregivers = caregiverService.getAvailableCaregivers();
            for (Caregiver caregiver : caregivers) {
                notificationService.sendAppNotification(
                        caregiver.getName(), alert.getAlertMessage());
            }
            if (caregivers.isEmpty()) {
                System.out.println("WARNING: no available caregivers to notify");
            }
        } catch (Exception e) {
            System.out.println("Could not notify caregivers: " + e.getMessage());
        }
    }
}
