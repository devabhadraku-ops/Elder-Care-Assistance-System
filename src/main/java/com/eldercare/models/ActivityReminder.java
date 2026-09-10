package com.eldercare.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ActivityReminder {

    private int reminderId;
    private int elderlyId;
    private String activityType;
    private LocalTime reminderTime;
    private String frequency;
    private String description;
    private boolean completed;
    private LocalDateTime lastNotified;
    private int notificationCount;
    private LocalDateTime createdDate;
    private boolean isActive;

    // Constructor for a new reminder
    public ActivityReminder(int elderlyId, String activityType,
                            LocalTime reminderTime, String frequency,
                            String description) {
        this.elderlyId = elderlyId;
        this.activityType = activityType;
        this.reminderTime = reminderTime;
        this.frequency = frequency;
        this.description = description;
        this.completed = false;
        this.notificationCount = 0;
        this.createdDate = LocalDateTime.now();
        this.isActive = true;
    }

    // Constructor for loading from database
    public ActivityReminder(int reminderId, int elderlyId,
                            String activityType, LocalTime reminderTime,
                            String frequency, String description,
                            boolean completed,
                            LocalDateTime lastNotified,
                            int notificationCount,
                            LocalDateTime createdDate,
                            boolean isActive) {

        this.reminderId = reminderId;
        this.elderlyId = elderlyId;
        this.activityType = activityType;
        this.reminderTime = reminderTime;
        this.frequency = frequency;
        this.description = description;
        this.completed = completed;
        this.lastNotified = lastNotified;
        this.notificationCount = notificationCount;
        this.createdDate = createdDate;
        this.isActive = isActive;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getLastNotified() {
        return lastNotified;
    }

    public void setLastNotified(LocalDateTime lastNotified) {
        this.lastNotified = lastNotified;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public void markNotified() {
        this.lastNotified = LocalDateTime.now();
        this.notificationCount++;
    }

    public boolean isValid() {
        return elderlyId > 0
                && activityType != null && !activityType.isEmpty()
                && reminderTime != null
                && frequency != null && !frequency.isEmpty();
    }

    @Override
    public String toString() {
        return "ActivityReminder{" +
                "id=" + reminderId +
                ", elderlyId=" + elderlyId +
                ", activityType='" + activityType + '\'' +
                ", reminderTime=" + reminderTime +
                ", frequency='" + frequency + '\'' +
                ", completed=" + completed +
                '}';
    }
}
