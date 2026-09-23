package com.eldercare;

import com.eldercare.models.Caregiver;
import com.eldercare.models.Elderly;
import com.eldercare.models.ServiceRequest;
import com.eldercare.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service request demo that needs NO database. The database-backed services
 * are replaced by small in-memory versions, but ServiceRequestManager is
 * the real class.
 */
public class ServiceRequestDemoNoDB {

    public static void main(String[] args) {

        // ----- test data kept in memory -----
        Elderly elder = new Elderly("Ramesh Kumar", 72, "9000000001",
                LocalDate.of(1954, 3, 10));
        elder.setElderlyId(1);

        Caregiver anitha = new Caregiver("Anitha Nurse", "6000000001");
        anitha.setCaregiverId(1);
        Caregiver vijay = new Caregiver("Vijay Helper", "6000000002");
        vijay.setCaregiverId(2);

        List<ServiceRequest> requests = new ArrayList<>();

        // ----- in-memory replacements for the database services -----
        ServiceRequestService requestService = new ServiceRequestService() {
            @Override
            public boolean addRequest(ServiceRequest request) {
                request.setRequestId(requests.size() + 1);
                requests.add(request);
                return true;
            }

            @Override
            public ServiceRequest getRequestById(int id) {
                for (ServiceRequest r : requests) {
                    if (r.getRequestId() == id) {
                        return r;
                    }
                }
                return null;
            }

            @Override
            public List<ServiceRequest> getPendingRequests() {
                List<ServiceRequest> result = new ArrayList<>();
                for (ServiceRequest r : requests) {
                    if (r.isPending()) {
                        result.add(r);
                    }
                }
                return result;
            }

            @Override
            public List<ServiceRequest> getRequestsByElderlyId(int elderlyId) {
                return requests;
            }

            @Override
            public boolean assignCaregiver(int requestId, int caregiverId) {
                getRequestById(requestId).assignCaregiver(caregiverId);
                return true;
            }

            @Override
            public boolean updateStatus(int requestId, String status) {
                getRequestById(requestId).setStatus(status);
                return true;
            }

            @Override
            public boolean completeRequest(int requestId) {
                getRequestById(requestId).complete();
                return true;
            }

            @Override
            public boolean addFeedback(int requestId, int rating, String feedback) {
                ServiceRequest r = getRequestById(requestId);
                r.setFeedbackRating(rating);
                r.setFeedbackNotes(feedback);
                return true;
            }
        };

        ElderlyService elderlyService = new ElderlyService() {
            @Override
            public Elderly getElderlyById(int id) {
                return id == 1 ? elder : null;
            }
        };

        CaregiverService caregiverService = new CaregiverService() {
            @Override
            public List<Caregiver> getAvailableCaregivers() {
                return List.of(anitha, vijay);
            }

            @Override
            public Caregiver getCaregiverById(int id) {
                return id == 1 ? anitha : (id == 2 ? vijay : null);
            }
        };

        ServiceRequestManager manager = new ServiceRequestManager(requestService,
                elderlyService, caregiverService, new NotificationService());

        // ----- 1. Elderly person asks for help -----
        System.out.println("=== 1. Ramesh submits requests ===");
        manager.submitRequest(1, "shopping", "Need milk and bread", "MEDIUM");
        manager.submitRequest(1, "medical", "Take me to the doctor", "HIGH");
        manager.submitRequest(1, "flying", "Take me to the moon", "LOW");   // invalid

        // ----- 2. Caregiver dashboard -----
        System.out.println("\n=== 2. Pending requests (most urgent first) ===");
        for (ServiceRequest r : manager.getPendingRequests()) {
            System.out.println("  #" + r.getRequestId() + " " + r.getUrgency()
                    + " " + r.getServiceType() + " - " + r.getDescription());
        }

        // ----- 3. Two caregivers try to take the same request -----
        System.out.println("\n=== 3. Anitha and Vijay both try to accept request 2 ===");
        System.out.println("Anitha accepted: " + manager.acceptRequest(2, 1));
        System.out.println("Vijay accepted: " + manager.acceptRequest(2, 2));

        // ----- 4. Steps cannot be skipped -----
        System.out.println("\n=== 4. Try to complete before starting ===");
        System.out.println("Completed: " + manager.completeRequest(2));

        // ----- 5. Normal flow -----
        System.out.println("\n=== 5. Start, complete, rate ===");
        manager.startWork(2);
        manager.completeRequest(2);
        manager.addFeedback(2, 5, "Very helpful");

        // ----- 6. Ramesh checks his requests -----
        System.out.println("\n=== 6. Ramesh's requests ===");
        for (ServiceRequest r : manager.getRequestsForElderly(1)) {
            System.out.println("  #" + r.getRequestId() + " " + r.getServiceType()
                    + " - " + r.getStatus());
        }
    }
}
