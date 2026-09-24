package com.eldercare.dao;

import com.eldercare.models.Appointment;
import com.eldercare.models.Elderly;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentDAOTest {

    @Test
    void testAddAndFindAppointment() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        Elderly elderly = new Elderly(
                "Appointment Test Elderly",
                70,
                "9999999991",
                LocalDate.of(1956, 1, 1)
        );

        assertTrue(elderlyDAO.add(elderly));

        Appointment appointment = new Appointment(
                elderly.getElderlyId(),
                "General Checkup",
                "Dr. Test",
                "Test Clinic",
                "Test Location",
                "9999999990",
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 30),
                30
        );

        assertTrue(appointmentDAO.add(appointment));
        assertTrue(appointment.getAppointmentId() > 0);

        Appointment found =
                appointmentDAO.findById(appointment.getAppointmentId());

        assertNotNull(found);
        assertEquals(elderly.getElderlyId(), found.getElderlyId());
        assertEquals("General Checkup", found.getAppointmentType());
        assertEquals("Dr. Test", found.getDoctorName());
        assertEquals("Test Clinic", found.getClinicName());
        assertEquals("SCHEDULED", found.getStatus());

        appointmentDAO.delete(appointment.getAppointmentId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}