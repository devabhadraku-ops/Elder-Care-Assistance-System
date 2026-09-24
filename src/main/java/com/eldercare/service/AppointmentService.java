package com.eldercare.service;

import com.eldercare.dao.AppointmentDAO;
import com.eldercare.models.Appointment;

import java.util.List;

public class AppointmentService {

    private final AppointmentDAO appointmentDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
    }

    public boolean addAppointment(Appointment appointment) {

        if (appointment == null || !appointment.isValid()) {
            return false;
        }

        return appointmentDAO.add(appointment);
    }

    public Appointment getAppointmentById(int appointmentId) {

        if (appointmentId <= 0) {
            return null;
        }

        return appointmentDAO.findById(appointmentId);
    }

    public List<Appointment> getAppointmentsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return appointmentDAO.findByElderlyId(elderlyId);
    }

    public List<Appointment> getUpcomingAppointments() {

        return appointmentDAO.findUpcoming();
    }

    public List<Appointment> getAppointmentsByCaregiverId(int caregiverId) {

        if (caregiverId <= 0) {
            return List.of();
        }

        return appointmentDAO.findByCaregiverId(caregiverId);
    }

    public boolean updateAppointment(Appointment appointment) {

        if (appointment == null
                || appointment.getAppointmentId() <= 0
                || !appointment.isValid()) {
            return false;
        }

        return appointmentDAO.update(appointment);
    }

    public boolean cancelAppointment(int appointmentId) {

        if (appointmentId <= 0) {
            return false;
        }

        return appointmentDAO.cancel(appointmentId);
    }

    public boolean completeAppointment(int appointmentId) {

        if (appointmentId <= 0) {
            return false;
        }

        return appointmentDAO.complete(appointmentId);
    }

    public boolean markReminderSent(int appointmentId) {

        if (appointmentId <= 0) {
            return false;
        }

        return appointmentDAO.markReminderSent(appointmentId);
    }

    public boolean deleteAppointment(int appointmentId) {

        if (appointmentId <= 0) {
            return false;
        }

        return appointmentDAO.delete(appointmentId);
    }
}
