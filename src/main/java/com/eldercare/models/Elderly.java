package com.eldercare.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Elderly {

    private int elderlyId;

    private String name;
    private int age;
    private String phone;
    private String email;
    private String address;
    private LocalDate dateOfBirth;

    private String bloodType;
    private String medicalConditions;
    private String allergies;
    private String currentMedications;

    private String emergencyContact1Name;
    private String emergencyContact1Phone;
    private String emergencyContact2Name;
    private String emergencyContact2Phone;

    private LocalDateTime registrationDate;
    private boolean isActive;
    private String notes;

    // Constructor for new elderly person
    public Elderly(String name, int age, String phone, LocalDate dateOfBirth) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.isActive = true;
        this.registrationDate = LocalDateTime.now();
    }

    // Full constructor for database records
    public Elderly(int elderlyId, String name, int age, String phone,
                   String email, String address, LocalDate dateOfBirth,
                   String bloodType, String medicalConditions,
                   String allergies, String currentMedications,
                   String ec1Name, String ec1Phone,
                   String ec2Name, String ec2Phone,
                   LocalDateTime registrationDate,
                   boolean isActive, String notes) {

        this.elderlyId = elderlyId;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.medicalConditions = medicalConditions;
        this.allergies = allergies;
        this.currentMedications = currentMedications;
        this.emergencyContact1Name = ec1Name;
        this.emergencyContact1Phone = ec1Phone;
        this.emergencyContact2Name = ec2Name;
        this.emergencyContact2Phone = ec2Phone;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
        this.notes = notes;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 60 && age <= 120) {
            this.age = age;
        } else {
            throw new IllegalArgumentException(
                    "Age must be between 60 and 120"
            );
        }
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getCurrentMedications() {
        return currentMedications;
    }

    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }

    public String getEmergencyContact1Name() {
        return emergencyContact1Name;
    }

    public void setEmergencyContact1Name(String name) {
        this.emergencyContact1Name = name;
    }

    public String getEmergencyContact1Phone() {
        return emergencyContact1Phone;
    }

    public void setEmergencyContact1Phone(String phone) {
        this.emergencyContact1Phone = phone;
    }

    public String getEmergencyContact2Name() {
        return emergencyContact2Name;
    }

    public void setEmergencyContact2Name(String name) {
        this.emergencyContact2Name = name;
    }

    public String getEmergencyContact2Phone() {
        return emergencyContact2Phone;
    }

    public void setEmergencyContact2Phone(String phone) {
        this.emergencyContact2Phone = phone;
    }

    public List<String> getEmergencyContacts() {
        List<String> contacts = new ArrayList<>();

        if (emergencyContact1Phone != null &&
                !emergencyContact1Phone.isEmpty()) {
            contacts.add(emergencyContact1Phone);
        }

        if (emergencyContact2Phone != null &&
                !emergencyContact2Phone.isEmpty()) {
            contacts.add(emergencyContact2Phone);
        }

        return contacts;
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

    public int calculateAge() {
        if (dateOfBirth == null) {
            return age;
        }

        LocalDate today = LocalDate.now();

        return today.getYear() - dateOfBirth.getYear();
    }

    public boolean hasCondition(String condition) {
        if (medicalConditions == null ||
                medicalConditions.isEmpty()) {
            return false;
        }

        return medicalConditions
                .toLowerCase()
                .contains(condition.toLowerCase());
    }

    public boolean isAllergicTo(String allergen) {
        if (allergies == null ||
                allergies.isEmpty()) {
            return false;
        }

        return allergies
                .toLowerCase()
                .contains(allergen.toLowerCase());
    }

    public boolean isValid() {
        return name != null &&
                !name.isEmpty() &&
                age >= 60 &&
                age <= 120 &&
                phone != null &&
                !phone.isEmpty() &&
                dateOfBirth != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Elderly elderly = (Elderly) obj;

        return elderlyId == elderly.elderlyId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(elderlyId);
    }

    @Override
    public String toString() {
        return "Elderly{" +
                "id=" + elderlyId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", active=" + isActive +
                '}';
    }
}