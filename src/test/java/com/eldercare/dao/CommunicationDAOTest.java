package com.eldercare.dao;

import com.eldercare.models.Caregiver;
import com.eldercare.models.Communication;
import com.eldercare.models.Elderly;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationDAOTest {

    @Test
    void testAddAndFindCommunication() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        CaregiverDAO caregiverDAO = new CaregiverDAO();
        CommunicationDAO communicationDAO = new CommunicationDAO();

        Elderly elderly = new Elderly(
                "Communication Test Elderly",
                70,
                "9999999986",
                LocalDate.of(1956, 1, 1)
        );

        assertTrue(elderlyDAO.add(elderly));

        Caregiver caregiver = new Caregiver(
                "Communication Test Caregiver",
                "9999999985"
        );

        assertTrue(caregiverDAO.add(caregiver));

        Communication communication = new Communication(
                elderly.getElderlyId(),
                caregiver.getCaregiverId(),
                "TEXT",
                "Test communication message"
        );

        assertTrue(communicationDAO.add(communication));
        assertTrue(communication.getCommunicationId() > 0);

        Communication found =
                communicationDAO.findById(communication.getCommunicationId());

        assertNotNull(found);
        assertEquals(elderly.getElderlyId(), found.getElderlyId());
        assertEquals(caregiver.getCaregiverId(), found.getCaregiverId());
        assertEquals("TEXT", found.getMessageType());
        assertEquals("Test communication message", found.getMessage());
        assertFalse(found.isRead());

        communicationDAO.delete(communication.getCommunicationId());
        caregiverDAO.delete(caregiver.getCaregiverId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}