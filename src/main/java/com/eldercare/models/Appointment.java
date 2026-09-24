package com.eldercare.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Appointment {

    private int appointmentId;
    private int elderlyId;

    private String appointmentType;
    private String doctorName;
    private String clinicName;
    private String clinicLocation;
    private String clinicPhone;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int durationMinutes;

    private String status;
    private String notes;
    private boolean reminderSent;
    private int assignedCaregiverId;

    // Constructor for creating a new appointment
    public Appointment(
            int elderlyId,
            String appointmentType,
            String doctorName,
            String clinicName,
            String clinicLocation,
            String clinicPhone,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            int durationMinutes) {

        this.elderlyId = elderlyId;
        this.appointmentType = appointmentType;
        this.doctorName = doctorName;
        this.clinicName = clinicName;
        this.clinicLocation = clinicLocation;
        this.clinicPhone = clinicPhone;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;

        this.status = "SCHEDULED";
        this.reminderSent = false;
        this.assignedCaregiverId = 0;
    }


    // Constructor for loading appointment from database
    public Appointment(
            int appointmentId,
            int elderlyId,
            String appointmentType,
            String doctorName,
            String clinicName,
            String clinicLocation,
            String clinicPhone,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            int durationMinutes,
            String status,
            String notes,
            boolean reminderSent,
            int assignedCaregiverId) {

        this.appointmentId = appointmentId;
        this.elderlyId = elderlyId;
        this.appointmentType = appointmentType;
        this.doctorName = doctorName;
        this.clinicName = clinicName;
        this.clinicLocation = clinicLocation;
        this.clinicPhone = clinicPhone;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.notes = notes;
        this.reminderSent = reminderSent;
        this.assignedCaregiverId = assignedCaregiverId;
    }


    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }


    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }


    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }


    public String getClinicLocation() {
        return clinicLocation;
    }

    public void setClinicLocation(String clinicLocation) {
        this.clinicLocation = clinicLocation;
    }


    public String getClinicPhone() {
        return clinicPhone;
    }

    public void setClinicPhone(String clinicPhone) {
        this.clinicPhone = clinicPhone;
    }


    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }


    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }


    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }


    public int getAssignedCaregiverId() {
        return assignedCaregiverId;
    }

    public void setAssignedCaregiverId(int assignedCaregiverId) {
        this.assignedCaregiverId = assignedCaregiverId;
    }


    public boolean isValid() {

        return elderlyId > 0
                && appointmentDate != null
                && appointmentTime != null
                && durationMinutes > 0;
    }


    public boolean isScheduled() {
        return "SCHEDULED".equalsIgnoreCase(status);
    }


    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }


    public boolean isCancelled() {
        return "CANCELLED".equalsIgnoreCase(status);
    }


    public boolean isUpcoming() {

        if (appointmentDate == null || appointmentTime == null) {
            return false;
        }

        LocalDateTime appointmentDateTime =
                LocalDateTime.of(appointmentDate, appointmentTime);

        return appointmentDateTime.isAfter(LocalDateTime.now())
                && !isCancelled()
                && !isCompleted();
    }


    @Override
    public String toString() {

        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", elderlyId=" + elderlyId +
                ", appointmentType='" + appointmentType + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", status='" + status + '\'' +
                '}';
    }
}