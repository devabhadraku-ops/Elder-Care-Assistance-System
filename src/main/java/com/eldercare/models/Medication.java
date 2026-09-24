package com.eldercare.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Medication {

    private int medicationId;
    private int elderlyId;

    private String medicineName;
    private String dosage;
    private String frequency;

    private LocalDate startDate;
    private LocalDate endDate;

    private String prescribedBy;
    private LocalDate prescriptionDate;

    private String notes;
    private boolean isActive;
    private LocalDateTime createdDate;


    // Constructor for creating a new medication
    public Medication(int elderlyId,
                      String medicineName,
                      String dosage,
                      String frequency,
                      LocalDate startDate,
                      LocalDate endDate,
                      String prescribedBy,
                      LocalDate prescriptionDate,
                      String notes) {

        this.elderlyId = elderlyId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prescribedBy = prescribedBy;
        this.prescriptionDate = prescriptionDate;
        this.notes = notes;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
    }


    // Constructor for loading from database
    public Medication(int medicationId,
                      int elderlyId,
                      String medicineName,
                      String dosage,
                      String frequency,
                      LocalDate startDate,
                      LocalDate endDate,
                      String prescribedBy,
                      LocalDate prescriptionDate,
                      String notes,
                      boolean isActive,
                      LocalDateTime createdDate) {

        this.medicationId = medicationId;
        this.elderlyId = elderlyId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.prescribedBy = prescribedBy;
        this.prescriptionDate = prescriptionDate;
        this.notes = notes;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }


    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPrescribedBy() {
        return prescribedBy;
    }

    public void setPrescribedBy(String prescribedBy) {
        this.prescribedBy = prescribedBy;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(LocalDate prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }


    public boolean isValid() {
        return elderlyId > 0
                && medicineName != null
                && !medicineName.isEmpty();
    }


    public boolean isCurrentlyActive() {

        if (!isActive) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (startDate != null && today.isBefore(startDate)) {
            return false;
        }

        if (endDate != null && today.isAfter(endDate)) {
            return false;
        }

        return true;
    }


    @Override
    public String toString() {
        return "Medication{" +
                "medicationId=" + medicationId +
                ", elderlyId=" + elderlyId +
                ", medicineName='" + medicineName + '\'' +
                ", dosage='" + dosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", prescribedBy='" + prescribedBy + '\'' +
                ", active=" + isActive +
                '}';
    }
}