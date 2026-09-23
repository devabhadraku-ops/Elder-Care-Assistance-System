package com.eldercare.service;

import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.models.ServiceRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Runs the life of a service request (shopping, transport, cooking...).
 *
 *   PENDING --accept--> ASSIGNED --start--> IN_PROGRESS --complete--> COMPLETED
 *
 *  - The elderly person submits a request; available caregivers are told.
 *  - Caregivers see pending requests, most urgent first.
 *  - The first caregiver to accept gets it (nobody else can take it).
 *  - Steps cannot be skipped (a request must be started before it is completed).
 *  - The elderly person is told at every step and can rate the service.
 */
public class ServiceRequestManager {

    public static final Set<String> SERVICE_TYPES =
            Set.of("shopping", "transport", "cooking", "medical", "cleaning", "other");
    public static final Set<String> URGENCY_LEVELS = Set.of("LOW", "MEDIUM", "HIGH");

    private final ServiceRequestService requestService;
    private final ElderlyService elderlyService;
    private final CaregiverService caregiverService;
    private final NotificationService notificationService;

    public ServiceRequestManager() {
        this(new ServiceRequestService(), new ElderlyService(),
                new CaregiverService(), new NotificationService());
    }

    // Constructor that lets tests pass in their own services
    public ServiceRequestManager(ServiceRequestService requestService,
                                 ElderlyService elderlyService,
                                 CaregiverService caregiverService,
                                 NotificationService notificationService) {
        this.requestService = requestService;
        this.elderlyService = elderlyService;
        this.caregiverService = caregiverService;
        this.notificationService = notificationService;
    }

    /**
     * The elderly person asks for help.
     * @return the saved request, or null if the input was invalid
     */
    public ServiceRequest submitRequest(int elderlyId, String serviceType,
                                        String description, String urgency) {

        String type = serviceType == null ? "" : serviceType.trim().toLowerCase();
        String level = urgency == null ? "" : urgency.trim().toUpperCase();

        if (!SERVICE_TYPES.contains(type)) {
            System.out.println("Request rejected: unknown service type '"
                    + serviceType + "'. Allowed: " + SERVICE_TYPES);
            return null;
        }
        if (!URGENCY_LEVELS.contains(level)) {
            System.out.println("Request rejected: urgency must be LOW, MEDIUM or HIGH");
            return null;
        }

        ServiceRequest request = new ServiceRequest(elderlyId, type, description, level);
        if (!requestService.addRequest(request)) {
            System.out.println("Request could not be saved");
            return null;
        }

        System.out.println("Request " + request.getRequestId() + " created: "
                + type + " (" + level + ")");
        notifyAvailableCaregivers("New " + level + " request from "
                + nameOfElderly(elderlyId) + ": " + type + " - " + description);
        return request;
    }

    /** Pending requests, most urgent first, then oldest first. */
    public List<ServiceRequest> getPendingRequests() {
        List<ServiceRequest> pending = new ArrayList<>(requestService.getPendingRequests());
        pending.sort(Comparator
                .comparingInt((ServiceRequest r) -> urgencyRank(r.getUrgency())).reversed()
                .thenComparing(ServiceRequest::getRequestTime));
        return pending;
    }

    public List<ServiceRequest> getRequestsForElderly(int elderlyId) {
        return requestService.getRequestsByElderlyId(elderlyId);
    }

    /** A caregiver takes a pending request. Only the first one succeeds. */
    public boolean acceptRequest(int requestId, int caregiverId) {

        ServiceRequest request = requestService.getRequestById(requestId);
        if (request == null) {
            System.out.println("Request " + requestId + " not found");
            return false;
        }
        if (!request.isPending()) {
            System.out.println("Request " + requestId + " cannot be accepted: it is "
                    + request.getStatus());
            return false;
        }
        if (!requestService.assignCaregiver(requestId, caregiverId)) {
            return false;
        }

        notifyElderly(request, nameOfCaregiver(caregiverId)
                + " has accepted your " + request.getServiceType() + " request");
        return true;
    }

    /** The caregiver starts working on an assigned request. */
    public boolean startWork(int requestId) {
        ServiceRequest request = requestService.getRequestById(requestId);
        if (!isInStatus(request, "ASSIGNED", "start")) {
            return false;
        }
        if (!requestService.updateStatus(requestId, "IN_PROGRESS")) {
            return false;
        }
        notifyElderly(request, "Your " + request.getServiceType()
                + " request is being handled now");
        return true;
    }

    /** The caregiver finishes a request that is in progress. */
    public boolean completeRequest(int requestId) {
        ServiceRequest request = requestService.getRequestById(requestId);
        if (!isInStatus(request, "IN_PROGRESS", "complete")) {
            return false;
        }
        if (!requestService.completeRequest(requestId)) {
            return false;
        }
        notifyElderly(request, "Your " + request.getServiceType()
                + " request is complete. Please rate the service.");
        return true;
    }

    /** The elderly person rates a completed request (1 to 5). */
    public boolean addFeedback(int requestId, int rating, String notes) {
        ServiceRequest request = requestService.getRequestById(requestId);
        if (!isInStatus(request, "COMPLETED", "rate")) {
            return false;
        }
        return requestService.addFeedback(requestId, rating, notes);
    }

    // ---------------------------------------------------------------

    private boolean isInStatus(ServiceRequest request, String required, String action) {
        if (request == null) {
            System.out.println("Request not found");
            return false;
        }
        if (!required.equalsIgnoreCase(request.getStatus())) {
            System.out.println("Cannot " + action + " request " + request.getRequestId()
                    + ": it is " + request.getStatus() + " (needs " + required + ")");
            return false;
        }
        return true;
    }

    private int urgencyRank(String urgency) {
        if ("HIGH".equalsIgnoreCase(urgency)) {
            return 3;
        }
        if ("MEDIUM".equalsIgnoreCase(urgency)) {
            return 2;
        }
        return 1;
    }

    private void notifyAvailableCaregivers(String message) {
        try {
            for (Caregiver caregiver : caregiverService.getAvailableCaregivers()) {
                notificationService.sendAppNotification(caregiver.getName(), message);
            }
        } catch (Exception e) {
            System.out.println("Could not notify caregivers: " + e.getMessage());
        }
    }

    private void notifyElderly(ServiceRequest request, String message) {
        notificationService.sendAppNotification(
                nameOfElderly(request.getElderlyId()), message);
    }

    private String nameOfElderly(int elderlyId) {
        try {
            Elderly elder = elderlyService.getElderlyById(elderlyId);
            if (elder != null) {
                return elder.getName();
            }
        } catch (Exception e) {
            // fall through
        }
        return "Elderly #" + elderlyId;
    }

    private String nameOfCaregiver(int caregiverId) {
        try {
            Caregiver caregiver = caregiverService.getCaregiverById(caregiverId);
            if (caregiver != null) {
                return caregiver.getName();
            }
        } catch (Exception e) {
            // fall through
        }
        return "Caregiver #" + caregiverId;
    }
}
