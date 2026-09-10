package com.eldercare.models;

import java.time.LocalDateTime;

public class HealthMetrics {

    private int metricsId;
    private int elderlyId;
    private int systolicBp;
    private int diastolicBp;
    private int heartRate;
    private double temperature;
    private int oxygenLevel;
    private double weight;
    private LocalDateTime recordedTime;
    private String recordedBy;
    private String notes;
    private boolean alertGenerated;
    private int alertId;

    // Constructor for new health record
    public HealthMetrics(int elderlyId, int systolicBp,
                         int diastolicBp, int heartRate,
                         double temperature, int oxygenLevel,
                         double weight, String recordedBy) {

        this.elderlyId = elderlyId;
        this.systolicBp = systolicBp;
        this.diastolicBp = diastolicBp;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.weight = weight;
        this.recordedBy = recordedBy;
        this.recordedTime = LocalDateTime.now();
        this.alertGenerated = false;
    }

    // Constructor for loading from database
    public HealthMetrics(int metricsId, int elderlyId,
                         int systolicBp, int diastolicBp,
                         int heartRate, double temperature,
                         int oxygenLevel, double weight,
                         LocalDateTime recordedTime,
                         String recordedBy, String notes,
                         boolean alertGenerated, int alertId) {

        this.metricsId = metricsId;
        this.elderlyId = elderlyId;
        this.systolicBp = systolicBp;
        this.diastolicBp = diastolicBp;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.weight = weight;
        this.recordedTime = recordedTime;
        this.recordedBy = recordedBy;
        this.notes = notes;
        this.alertGenerated = alertGenerated;
        this.alertId = alertId;
    }

    public int getMetricsId() {
        return metricsId;
    }

    public void setMetricsId(int metricsId) {
        this.metricsId = metricsId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public int getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(int systolicBp) {
        this.systolicBp = systolicBp;
    }

    public int getDiastolicBp() {
        return diastolicBp;
    }

    public void setDiastolicBp(int diastolicBp) {
        this.diastolicBp = diastolicBp;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(int oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public LocalDateTime getRecordedTime() {
        return recordedTime;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAlertGenerated() {
        return alertGenerated;
    }

    public void setAlertGenerated(boolean alertGenerated) {
        this.alertGenerated = alertGenerated;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public boolean hasLowOxygen() {
        return oxygenLevel > 0 && oxygenLevel < 90;
    }

    public boolean hasHighHeartRate() {
        return heartRate > 100;
    }

    public boolean hasLowHeartRate() {
        return heartRate > 0 && heartRate < 60;
    }

    public boolean hasHighTemperature() {
        return temperature > 38.0;
    }

    public boolean isValid() {
        return elderlyId > 0
                && systolicBp > 0
                && diastolicBp > 0
                && heartRate > 0
                && temperature > 0
                && oxygenLevel > 0
                && weight > 0;
    }

    @Override
    public String toString() {
        return "HealthMetrics{" +
                "id=" + metricsId +
                ", elderlyId=" + elderlyId +
                ", bloodPressure=" + systolicBp + "/" + diastolicBp +
                ", heartRate=" + heartRate +
                ", temperature=" + temperature +
                ", oxygenLevel=" + oxygenLevel +
                ", weight=" + weight +
                '}';
    }
}