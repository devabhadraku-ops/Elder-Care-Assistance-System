package com.eldercare.service;

import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.models.HealthAlert;
import com.eldercare.models.HealthMetrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Records a health reading and raises a HealthAlert if it is abnormal.
 *
 * Rules (temperature is in Celsius, like the rest of the project):
 *   Blood pressure : high above 140/90, very high (critical) from 180/120,
 *                    low below 90/60
 *   Heart rate     : high above 100 (critical above 130),
 *                    low below 60 (critical below 40)
 *   Temperature    : fever above 38.0 (critical from 39.5),
 *                    low below 35.0 (critical)
 *   Oxygen         : below 90 (critical)
 *
 * Severity: no problem = NORMAL, one ordinary problem = WARNING,
 *           a critical problem or two or more problems = CRITICAL.
 * WARNING tells the caregivers. CRITICAL also texts the emergency contacts.
 */
public class HealthMonitor {

    /** The result of checking one reading. */
    public static class Assessment {
        private final String severity;
        private final List<String> problems;

        Assessment(String severity, List<String> problems) {
            this.severity = severity;
            this.problems = problems;
        }

        public String getSeverity() {
            return severity;
        }

        public List<String> getProblems() {
            return problems;
        }

        public boolean isNormal() {
            return problems.isEmpty();
        }
    }

    private final HealthMetricsService healthMetricsService;
    private final ElderlyService elderlyService;
    private final CaregiverService caregiverService;
    private final AlertService alertService;
    private final NotificationService notificationService;

    public HealthMonitor() {
        this(new HealthMetricsService(), new ElderlyService(),
                new CaregiverService(), new AlertService(),
                new NotificationService());
    }

    // Constructor that lets tests pass in their own services
    public HealthMonitor(HealthMetricsService healthMetricsService,
                         ElderlyService elderlyService,
                         CaregiverService caregiverService,
                         AlertService alertService,
                         NotificationService notificationService) {
        this.healthMetricsService = healthMetricsService;
        this.elderlyService = elderlyService;
        this.caregiverService = caregiverService;
        this.alertService = alertService;
        this.notificationService = notificationService;
    }

    /**
     * Saves the reading and raises an alert if it is abnormal.
     * @return the HealthAlert, or null if the reading was normal or invalid
     */
    public HealthAlert recordReading(HealthMetrics metrics) {

        if (metrics == null || !metrics.isValid()) {
            System.out.println("Reading rejected: some values are missing or zero");
            return null;
        }

        if (!healthMetricsService.addMetrics(metrics)) {
            System.out.println("WARNING: reading could not be saved, "
                    + "but it will still be checked");
        }

        Assessment result = assess(metrics);
        System.out.println("Reading " + metrics.getSystolicBp() + "/"
                + metrics.getDiastolicBp() + ", HR " + metrics.getHeartRate()
                + ", temp " + metrics.getTemperature()
                + ", O2 " + metrics.getOxygenLevel() + " -> " + result.getSeverity());

        if (result.isNormal()) {
            return null;
        }

        Elderly elder = findElder(metrics.getElderlyId());
        String name = elder != null ? elder.getName() : "Elderly #" + metrics.getElderlyId();

        HealthAlert alert = new HealthAlert(metrics.getElderlyId(), name,
                result.getSeverity(), result.getProblems());

        // Notify first, save afterwards (same rule as SOS)
        notifyCaregivers(alert);
        if ("CRITICAL".equals(alert.getSeverity()) && elder != null) {
            for (String phone : elder.getEmergencyContacts()) {
                notificationService.sendSMS(phone, alert.getAlertMessage());
            }
        }

        if (alertService.addAlert(alert)) {
            healthMetricsService.linkAlert(metrics.getMetricsId(), alert.getAlertId());
        } else {
            System.out.println("WARNING: alert could not be saved to the database");
        }

        return alert;
    }

    /** Checks one reading against the rules above. */
    public Assessment assess(HealthMetrics m) {

        List<String> problems = new ArrayList<>();
        boolean critical = false;

        int sys = m.getSystolicBp();
        int dia = m.getDiastolicBp();
        if (sys >= 180 || dia >= 120) {
            problems.add("Very high blood pressure (" + sys + "/" + dia + ")");
            critical = true;
        } else if (sys > 140 || dia > 90) {
            problems.add("High blood pressure (" + sys + "/" + dia + ")");
        } else if (sys < 90 || dia < 60) {
            problems.add("Low blood pressure (" + sys + "/" + dia + ")");
        }

        int hr = m.getHeartRate();
        if (m.hasHighHeartRate()) {
            problems.add("High heart rate (" + hr + " bpm)");
            if (hr > 130) {
                critical = true;
            }
        } else if (m.hasLowHeartRate()) {
            problems.add("Low heart rate (" + hr + " bpm)");
            if (hr < 40) {
                critical = true;
            }
        }

        double temp = m.getTemperature();
        if (m.hasHighTemperature()) {
            problems.add("Fever (" + temp + " C)");
            if (temp >= 39.5) {
                critical = true;
            }
        } else if (temp > 0 && temp < 35.0) {
            problems.add("Low body temperature (" + temp + " C)");
            critical = true;
        }

        if (m.hasLowOxygen()) {
            problems.add("Low oxygen level (" + m.getOxygenLevel() + "%)");
            critical = true;
        }

        String severity;
        if (problems.isEmpty()) {
            severity = "NORMAL";
        } else if (critical || problems.size() >= 2) {
            severity = "CRITICAL";
        } else {
            severity = "WARNING";
        }
        return new Assessment(severity, problems);
    }

    // ---------------------------------------------------------------

    private Elderly findElder(int elderlyId) {
        try {
            return elderlyService.getElderlyById(elderlyId);
        } catch (Exception e) {
            return null;
        }
    }

    private void notifyCaregivers(HealthAlert alert) {
        try {
            for (Caregiver caregiver : caregiverService.getAvailableCaregivers()) {
                notificationService.sendAppNotification(
                        caregiver.getName(), alert.getAlertMessage());
            }
        } catch (Exception e) {
            System.out.println("Could not notify caregivers: " + e.getMessage());
        }
    }
}
