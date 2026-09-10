package com.eldercare.models;

import java.time.LocalDateTime;

public class Alert {

    private int alertId;
    private int elderlyId;
    private String alertType;
    private String severity;
    private String description;
    private boolean acknowledged;
    private LocalDateTime acknowledgmentTime;
    private int acknowledgmentBy;
    private LocalDateTime triggeredTime;
    private boolean responseRequired;
    private LocalDateTime responseDeadline;

    // Constructor for a new alert
    public Alert(int elderlyId, String alertType, String severity,
                 String description) {
        this.elderlyId = elderlyId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.acknowledged = false;
        this.triggeredTime = LocalDateTime.now();
        this.responseRequired = false;
    }

    // Constructor for loading an alert from database
    public Alert(int alertId, int elderlyId, String alertType,
                 String severity, String description,
                 boolean acknowledged,
                 LocalDateTime acknowledgmentTime,
                 int acknowledgmentBy,
                 LocalDateTime triggeredTime,
                 boolean responseRequired,
                 LocalDateTime responseDeadline) {

        this.alertId = alertId;
        this.elderlyId = elderlyId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.acknowledged = acknowledged;
        this.acknowledgmentTime = acknowledgmentTime;
        this.acknowledgmentBy = acknowledgmentBy;
        this.triggeredTime = triggeredTime;
        this.responseRequired = responseRequired;
        this.responseDeadline = responseDeadline;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public LocalDateTime getAcknowledgmentTime() {
        return acknowledgmentTime;
    }

    public void setAcknowledgmentTime(LocalDateTime acknowledgmentTime) {
        this.acknowledgmentTime = acknowledgmentTime;
    }

    public int getAcknowledgmentBy() {
        return acknowledgmentBy;
    }

    public void setAcknowledgmentBy(int acknowledgmentBy) {
        this.acknowledgmentBy = acknowledgmentBy;
    }

    public LocalDateTime getTriggeredTime() {
        return triggeredTime;
    }

    public void setTriggeredTime(LocalDateTime triggeredTime) {
        this.triggeredTime = triggeredTime;
    }

    public boolean isResponseRequired() {
        return responseRequired;
    }

    public void setResponseRequired(boolean responseRequired) {
        this.responseRequired = responseRequired;
    }

    public LocalDateTime getResponseDeadline() {
        return responseDeadline;
    }

    public void setResponseDeadline(LocalDateTime responseDeadline) {
        this.responseDeadline = responseDeadline;
    }

    public void acknowledge(int caregiverId) {
        this.acknowledged = true;
        this.acknowledgmentBy = caregiverId;
        this.acknowledgmentTime = LocalDateTime.now();
    }

    public boolean requiresResponse() {
        return responseRequired && !acknowledged;
    }

    public boolean isValid() {
        return elderlyId > 0
                && alertType != null && !alertType.isEmpty()
                && severity != null && !severity.isEmpty();
    }

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + alertId +
                ", elderlyId=" + elderlyId +
                ", type='" + alertType + '\'' +
                ", severity='" + severity + '\'' +
                ", acknowledged=" + acknowledged +
                '}';
    }
}
