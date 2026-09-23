package com.eldercare;

import com.eldercare.models.Alert;
import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.models.SOSAlert;
import com.eldercare.service.AlertService;
import com.eldercare.service.CaregiverService;
import com.eldercare.service.ElderlyService;
import com.eldercare.service.EmergencyService;

import java.time.LocalDate;
import java.util.List;

/**
 * Runs the whole SOS flow once, against the real MySQL database.
 * Needs: MySQL running, schema loaded, and DB_PASSWORD environment variable set.
 */
public class SOSDemo {

    public static void main(String[] args) {

        ElderlyService elderlyService = new ElderlyService();
        CaregiverService caregiverService = new CaregiverService();
        AlertService alertService = new AlertService();
        EmergencyService emergencyService = new EmergencyService();

        // Unique phone numbers so the demo can be run many times
        String suffix = String.valueOf(System.currentTimeMillis() % 1_000_000_000L);

        // 1. Create test data (the database is empty at the start)
        Elderly elder = new Elderly("Ramesh Kumar", 72,
                "9" + suffix, LocalDate.of(1954, 3, 10));
        elder.setEmergencyContact1Name("Suresh (son)");
        elder.setEmergencyContact1Phone("8" + suffix);
        elder.setEmergencyContact2Name("Latha (daughter)");
        elder.setEmergencyContact2Phone("7" + suffix);

        Caregiver caregiver = new Caregiver("Anitha Nurse", "6" + suffix);

        if (!elderlyService.addElderly(elder) || !caregiverService.addCaregiver(caregiver)) {
            System.out.println("Could not create test data - is MySQL running "
                    + "and DB_PASSWORD set?");
            return;
        }

        // 2. Elderly person presses SOS
        SOSAlert alert = emergencyService.triggerSOS(elder.getElderlyId(), "Fall detected");

        // 3. Caregiver sees it
        System.out.println("\n--- Active alerts on caregiver dashboard ---");
        List<Alert> active = emergencyService.getActiveAlerts();
        for (Alert a : active) {
            System.out.println(a);
        }

        // 4. Caregiver acknowledges it
        if (alert != null && alert.getAlertId() > 0) {
            boolean ok = emergencyService.acknowledge(alert.getAlertId(),
                    caregiver.getCaregiverId());
            System.out.println("\nAcknowledged: " + ok);
        }

        System.out.println("Alerts for this elderly person: "
                + alertService.getAlertsByElderlyId(elder.getElderlyId()));
    }
}
