package com.eldercare.service;

import com.eldercare.dao.MedicationDAO;
import com.eldercare.models.Medication;

import java.util.List;

public class MedicationService {

    private final MedicationDAO medicationDAO;

    public MedicationService() {
        this.medicationDAO = new MedicationDAO();
    }

    // Add a new medication
    public boolean addMedication(Medication medication) {

        if (medication == null || !medication.isValid()) {
            return false;
        }

        return medicationDAO.add(medication);
    }


    // Get medication by ID
    public Medication getMedicationById(int medicationId) {

        if (medicationId <= 0) {
            return null;
        }

        return medicationDAO.findById(medicationId);
    }


    // Get all medications for an elderly person
    public List<Medication> getMedicationsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return medicationDAO.findByElderlyId(elderlyId);
    }


    // Get active medications for an elderly person
    public List<Medication> getActiveMedicationsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return medicationDAO.findActiveByElderlyId(elderlyId);
    }


    // Get all active medications
    public List<Medication> getAllActiveMedications() {

        return medicationDAO.findAllActive();
    }


    // Update medication
    public boolean updateMedication(Medication medication) {

        if (medication == null
                || medication.getMedicationId() <= 0
                || !medication.isValid()) {
            return false;
        }

        return medicationDAO.update(medication);
    }


    // Deactivate medication
    public boolean deactivateMedication(int medicationId) {

        if (medicationId <= 0) {
            return false;
        }

        return medicationDAO.deactivate(medicationId);
    }


    // Activate medication
    public boolean activateMedication(int medicationId) {

        if (medicationId <= 0) {
            return false;
        }

        return medicationDAO.activate(medicationId);
    }


    // Delete medication
    public boolean deleteMedication(int medicationId) {

        if (medicationId <= 0) {
            return false;
        }

        return medicationDAO.delete(medicationId);
    }
}
