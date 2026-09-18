package com.eldercare.dao;

import com.eldercare.models.Elderly;
import com.eldercare.models.HealthMetrics;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HealthMetricsDAOTest {

    @Test
    void testAddAndFindHealthMetrics() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        HealthMetricsDAO metricsDAO = new HealthMetricsDAO();

        Elderly elderly = new Elderly(
                "Metrics Test Elderly",
                70,
                "9999999994",
                LocalDate.of(1956, 1, 1)
        );

        boolean elderlyAdded = elderlyDAO.add(elderly);

        assertTrue(elderlyAdded);
        assertTrue(elderly.getElderlyId() > 0);

        HealthMetrics metrics = new HealthMetrics(
                elderly.getElderlyId(),
                120,
                80,
                72,
                36.5,
                98,
                65.0,
                "Test Caregiver"
        );

        boolean added = metricsDAO.add(metrics);

        assertTrue(added);
        assertTrue(metrics.getMetricsId() > 0);

        HealthMetrics found =
                metricsDAO.findById(metrics.getMetricsId());

        assertNotNull(found);
        assertEquals(120, found.getSystolicBp());
        assertEquals(80, found.getDiastolicBp());
        assertEquals(72, found.getHeartRate());
        assertEquals(36.5, found.getTemperature());
        assertEquals(98, found.getOxygenLevel());
        assertEquals(65.0, found.getWeight());
        assertEquals("Test Caregiver", found.getRecordedBy());

        metricsDAO.delete(metrics.getMetricsId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}