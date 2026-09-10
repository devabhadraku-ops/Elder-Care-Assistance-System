package com.eldercare.service;

import com.eldercare.dao.ServiceRequestDAO;
import com.eldercare.models.ServiceRequest;

import java.util.List;

public class ServiceRequestService {

    private final ServiceRequestDAO serviceRequestDAO;

    public ServiceRequestService() {
        this.serviceRequestDAO = new ServiceRequestDAO();
    }

    // Add a new service request
    public boolean addRequest(ServiceRequest request) {

        if (request == null) {
            return false;
        }

        if (!request.isValid()) {
            return false;
        }

        return serviceRequestDAO.add(request);
    }

    // Find request by ID
    public ServiceRequest getRequestById(int id) {

        if (id <= 0) {
            return null;
        }

        return serviceRequestDAO.findById(id);
    }

    // Get requests for an elderly person
    public List<ServiceRequest> getRequestsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return serviceRequestDAO.findByElderlyId(elderlyId);
    }

    // Get pending requests
    public List<ServiceRequest> getPendingRequests() {
        return serviceRequestDAO.findPending();
    }

    // Assign caregiver to request
    public boolean assignCaregiver(int requestId, int caregiverId) {

        if (requestId <= 0 || caregiverId <= 0) {
            return false;
        }

        return serviceRequestDAO.assignCaregiver(requestId, caregiverId);
    }

    // Update request status
    public boolean updateStatus(int requestId, String status) {

        if (requestId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }

        return serviceRequestDAO.updateStatus(requestId, status.trim());
    }

    // Complete a request
    public boolean completeRequest(int requestId) {

        if (requestId <= 0) {
            return false;
        }

        return serviceRequestDAO.complete(requestId);
    }

    // Add feedback
    public boolean addFeedback(int requestId, int rating, String feedback) {

        if (requestId <= 0 || rating < 1 || rating > 5) {
            return false;
        }

        return serviceRequestDAO.addFeedback(requestId, rating, feedback);
    }

    // Delete request
    public boolean deleteRequest(int requestId) {

        if (requestId <= 0) {
            return false;
        }

        return serviceRequestDAO.delete(requestId);
    }
}