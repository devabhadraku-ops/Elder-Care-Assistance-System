package com.eldercare.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sends notifications. For this project the messages are printed to the
 * console (and kept in a log). In a real product each method would call an
 * SMS / voice / push provider (Twilio, Firebase, ...) - only this class
 * would need to change.
 */
public class NotificationService {

    private final List<String> sentLog = new ArrayList<>();

    public void sendSMS(String phone, String message) {
        record("[SMS]  to " + phone + ": " + message);
    }

    public void sendCall(String phone, String message) {
        record("[CALL] to " + phone + ": " + message);
    }

    public void sendAppNotification(String recipient, String message) {
        record("[APP]  to " + recipient + ": " + message);
    }

    /** Everything that has been sent so far (useful for tests). */
    public List<String> getSentLog() {
        return Collections.unmodifiableList(sentLog);
    }

    private void record(String line) {
        sentLog.add(line);
        System.out.println(line);
    }
}
