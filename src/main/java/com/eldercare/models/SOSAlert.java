package com.eldercare.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An emergency SOS alert. It is an Alert (inheritance) that is always
 * CRITICAL, always needs a response, and knows who must be contacted.
 */
public class SOSAlert extends Alert {

    private static final int RESPONSE_MINUTES = 5;

    private final String elderName;
    private final List<String> emergencyContacts = new ArrayList<>();

    public SOSAlert(int elderlyId, String elderName, String reason) {
        super(elderlyId, "SOS", "CRITICAL", "SOS pressed: " + reason);
        this.elderName = elderName;
        setResponseRequired(true);
        setResponseDeadline(LocalDateTime.now().plusMinutes(RESPONSE_MINUTES));
    }

    public String getElderName() {
        return elderName;
    }

    public void addEmergencyContact(String phone) {
        if (phone != null && !phone.isEmpty()) {
            emergencyContacts.add(phone);
        }
    }

    public List<String> getEmergencyContacts() {
        return Collections.unmodifiableList(emergencyContacts);
    }

    @Override
    public String getAlertMessage() {
        return "EMERGENCY SOS! " + elderName + " needs immediate help. "
                + getDescription() + " (time: " + getTriggeredTime() + ")";
    }
}
