
package com.eldercare.dao;

import com.eldercare.models.Elderly;
import com.eldercare.models.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRequestDAOTest {

    @Test
    void testAddAndFindServiceRequest() {

        ElderlyDAO elderlyDAO = new ElderlyDAO();
        ServiceRequestDAO requestDAO = new ServiceRequestDAO();

        // Create elderly person for the foreign key
        Elderly elderly = new Elderly(
                "Request Test Elderly",
                70,
                "9999999995",
                LocalDate.of(1956, 1, 1)
        );

        boolean elderlyAdded = elderlyDAO.add(elderly);

        assertTrue(elderlyAdded);
        assertTrue(elderly.getElderlyId() > 0);

        // Create service request
        ServiceRequest request = new ServiceRequest(
                elderly.getElderlyId(),
                "Home Visit",
                "Test service request",
                "HIGH"
        );

        boolean added = requestDAO.add(request);

        assertTrue(added);
        assertTrue(request.getRequestId() > 0);

        // Find request
        ServiceRequest found =
                requestDAO.findById(request.getRequestId());

        assertNotNull(found);
        assertEquals("Home Visit", found.getServiceType());
        assertEquals("Test service request", found.getDescription());
        assertEquals("HIGH", found.getUrgency());
        assertEquals("PENDING", found.getStatus());

        // Clean up
        requestDAO.delete(request.getRequestId());
        elderlyDAO.delete(elderly.getElderlyId());
    }
}