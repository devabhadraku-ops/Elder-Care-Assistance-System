package com.eldercare.models;

import java.time.LocalDateTime;
import java.util.List;

/**
 * An alert raised when a health reading is abnormal. Like SOSAlert it is an
 * Alert (inheritance), but it has its own wording (polymorphism).
 * The severity is WARNING or CRITICAL.
 */
public class HealthAlert extends Alert {

    private static final int CRITICAL_RESPONSE_MINUTES = 15;

    private final String elderName;
    private final List<String> problems;

    public HealthAlert(int elderlyId, String elderName,
                       String severity, List<String> problems) {
        super(elderlyId, "HEALTH", severity,
                "Abnormal health reading: " + String.join(", ", problems));
        this.elderName = elderName;
        this.problems = List.copyOf(problems);

        if ("CRITICAL".equals(severity)) {
            setResponseRequired(true);
            setResponseDeadline(LocalDateTime.now().plusMinutes(CRITICAL_RESPONSE_MINUTES));
        }
    }

    public String getElderName() {
        return elderName;
    }

    public List<String> getProblems() {
        return problems;
    }

    @Override
    public String getAlertMessage() {
        return "HEALTH ALERT (" + getSeverity() + ") for " + elderName
                + ": " + String.join(", ", problems);
    }
}
