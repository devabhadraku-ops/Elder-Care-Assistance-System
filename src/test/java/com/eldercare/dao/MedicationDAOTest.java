package com.eldercare.dao;

import com.eldercare.models.Elderly;
import com.eldercare.models.Medication;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MedicationDAOTest {

    @Test
    void testAddAndFindMedication() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        MedicationDAO medicationDAO = new MedicationDAO();

        Elderly elderly = new Elderly(
                "Medication Test Elderly",
                70,
                "9999999989",
                LocalDate.of(1956, 1, 1)
        );

        assertTrue(elderlyDAO.add(elderly));

        Medication medication = new Medication(
                elderly.getElderlyId(),
                "Paracetamol",
                "500 mg",
                "Twice daily",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                "Dr. Test",
                LocalDate.now(),
                "Take after food"
        );

        assertTrue(medicationDAO.add(medication));
        assertTrue(medication.getMedicationId() > 0);

        Medication found =
                medicationDAO.findById(medication.getMedicationId());

        assertNotNull(found);
        assertEquals(elderly.getElderlyId(), found.getElderlyId());
        assertEquals("Paracetamol", found.getMedicineName());
        assertEquals("500 mg", found.getDosage());
        assertEquals("Twice daily", found.getFrequency());
        assertEquals("Dr. Test", found.getPrescribedBy());
        assertTrue(found.isActive());

        medicationDAO.delete(medication.getMedicationId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}