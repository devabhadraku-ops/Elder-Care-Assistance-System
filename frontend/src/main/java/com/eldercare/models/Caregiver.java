package com.eldercare.models;

import java.time.LocalDateTime;

public class Caregiver {

    private int caregiverId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String qualification;
    private int experienceYears;
    private String specialization;
    private String availabilityStatus;
    private String preferredShift;
    private LocalDateTime registrationDate;
    private boolean isActive;
    private String notes;

    // Constructor for new caregiver
    public Caregiver(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.availabilityStatus = "AVAILABLE";
        this.isActive = true;
        this.registrationDate = LocalDateTime.now();
    }

    // Constructor for loading caregiver from database
    public Caregiver(int caregiverId, String name, String phone,
                     String email, String address, String qualification,
                     int experienceYears, String specialization,
                     String availabilityStatus, String preferredShift,
                     LocalDateTime registrationDate, boolean isActive,
                     String notes) {

        this.caregiverId = caregiverId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.specialization = specialization;
        this.availabilityStatus = availabilityStatus;
        this.preferredShift = preferredShift;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
        this.notes = notes;
    }

    public int getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(int caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getPreferredShift() {
        return preferredShift;
    }

    public void setPreferredShift(String preferredShift) {
        this.preferredShift = preferredShift;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(availabilityStatus)
                && isActive;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty()
                && phone != null && !phone.isEmpty()
                && experienceYears >= 0;
    }

    @Override
    public String toString() {
        return "Caregiver{" +
                "id=" + caregiverId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", active=" + isActive +
                '}';
    }
}
