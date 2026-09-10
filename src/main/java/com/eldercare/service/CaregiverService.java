package com.eldercare.service;

import com.eldercare.dao.CaregiverDAO;
import com.eldercare.models.Caregiver;

import java.util.List;

public class CaregiverService {

    private final CaregiverDAO caregiverDAO;

    public CaregiverService() {
        this.caregiverDAO = new CaregiverDAO();
    }

    // Add a new caregiver
    public boolean addCaregiver(Caregiver caregiver) {

        if (caregiver == null) {
            return false;
        }

        if (!caregiver.isValid()) {
            return false;
        }

        return caregiverDAO.add(caregiver);
    }

    // Find caregiver by ID
    public Caregiver getCaregiverById(int id) {

        if (id <= 0) {
            return null;
        }

        return caregiverDAO.findById(id);
    }

    // Search caregivers by name
    public List<Caregiver> searchByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }

        return caregiverDAO.findByName(name.trim());
    }

    // Get all caregivers
    public List<Caregiver> getAllCaregivers() {
        return caregiverDAO.findAll();
    }

    // Get available caregivers
    public List<Caregiver> getAvailableCaregivers() {
        return caregiverDAO.findAvailable();
    }

    // Update caregiver
    public boolean updateCaregiver(Caregiver caregiver) {

        if (caregiver == null) {
            return false;
        }

        if (caregiver.getCaregiverId() <= 0) {
            return false;
        }

        if (!caregiver.isValid()) {
            return false;
        }

        return caregiverDAO.update(caregiver);
    }

    // Delete caregiver
    public boolean deleteCaregiver(int id) {

        if (id <= 0) {
            return false;
        }

        return caregiverDAO.delete(id);
    }
}