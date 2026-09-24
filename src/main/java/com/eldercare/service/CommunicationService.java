package com.eldercare.service;

import com.eldercare.dao.CommunicationDAO;
import com.eldercare.models.Communication;

import java.util.List;

public class CommunicationService {

    private final CommunicationDAO communicationDAO;

    public CommunicationService() {
        this.communicationDAO = new CommunicationDAO();
    }

    // Send a new communication
    public boolean sendCommunication(Communication communication) {

        if (communication == null || !communication.isValid()) {
            return false;
        }

        return communicationDAO.add(communication);
    }


    // Get communication by ID
    public Communication getCommunicationById(int communicationId) {

        if (communicationId <= 0) {
            return null;
        }

        return communicationDAO.findById(communicationId);
    }


    // Get communications for an elderly person
    public List<Communication> getCommunicationsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return communicationDAO.findByElderlyId(elderlyId);
    }


    // Get communications for a caregiver
    public List<Communication> getCommunicationsByCaregiverId(int caregiverId) {

        if (caregiverId <= 0) {
            return List.of();
        }

        return communicationDAO.findByCaregiverId(caregiverId);
    }


    // Get unread communications for a caregiver
    public List<Communication> getUnreadCommunications(int caregiverId) {

        if (caregiverId <= 0) {
            return List.of();
        }

        return communicationDAO.findUnreadByCaregiverId(caregiverId);
    }


    // Mark communication as read
    public boolean markAsRead(int communicationId) {

        if (communicationId <= 0) {
            return false;
        }

        return communicationDAO.markAsRead(communicationId);
    }


    // Delete communication
    public boolean deleteCommunication(int communicationId) {

        if (communicationId <= 0) {
            return false;
        }

        return communicationDAO.delete(communicationId);
    }
}