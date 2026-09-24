package com.eldercare.dao;

import com.eldercare.models.Caregiver;
import com.eldercare.models.CaregiverTask;
import com.eldercare.models.Elderly;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverTaskDAOTest {

    @Test
    void testAddAndFindTask() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        CaregiverDAO caregiverDAO = new CaregiverDAO();
        CaregiverTaskDAO taskDAO = new CaregiverTaskDAO();

        Elderly elderly = new Elderly(
                "Task Test Elderly",
                70,
                "9999999988",
                LocalDate.of(1956, 1, 1)
        );

        assertTrue(elderlyDAO.add(elderly));

        Caregiver caregiver = new Caregiver(
                "Task Test Caregiver",
                "9999999987"
        );

        assertTrue(caregiverDAO.add(caregiver));

        CaregiverTask task = new CaregiverTask(
                caregiver.getCaregiverId(),
                elderly.getElderlyId(),
                "MEDICATION",
                "Give prescribed medicine",
                LocalDateTime.now().plusDays(1)
        );

        assertTrue(taskDAO.add(task));
        assertTrue(task.getTaskId() > 0);

        CaregiverTask found =
                taskDAO.findById(task.getTaskId());

        assertNotNull(found);
        assertEquals(caregiver.getCaregiverId(), found.getCaregiverId());
        assertEquals(elderly.getElderlyId(), found.getElderlyId());
        assertEquals("MEDICATION", found.getTaskType());
        assertEquals("Give prescribed medicine", found.getDescription());
        assertEquals("PENDING", found.getStatus());

        taskDAO.delete(task.getTaskId());
        caregiverDAO.delete(caregiver.getCaregiverId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}