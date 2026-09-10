package com.eldercare.models;

import java.time.LocalDateTime;

public class ServiceRequest {

    private int requestId;
    private int elderlyId;
    private String serviceType;
    private String description;
    private String urgency;
    private int assignedCaregiverId;
    private String status;
    private LocalDateTime requestTime;
    private LocalDateTime assignedTime;
    private LocalDateTime completionTime;
    private int feedbackRating;
    private String feedbackNotes;

    // Constructor for a new request
    public ServiceRequest(int elderlyId, String serviceType,
                          String description, String urgency) {
        this.elderlyId = elderlyId;
        this.serviceType = serviceType;
        this.description = description;
        this.urgency = urgency;
        this.status = "PENDING";
        this.requestTime = LocalDateTime.now();
    }

    // Constructor for loading from database
    public ServiceRequest(int requestId, int elderlyId,
                          String serviceType, String description,
                          String urgency, int assignedCaregiverId,
                          String status, LocalDateTime requestTime,
                          LocalDateTime assignedTime,
                          LocalDateTime completionTime,
                          int feedbackRating,
                          String feedbackNotes) {

        this.requestId = requestId;
        this.elderlyId = elderlyId;
        this.serviceType = serviceType;
        this.description = description;
        this.urgency = urgency;
        this.assignedCaregiverId = assignedCaregiverId;
        this.status = status;
        this.requestTime = requestTime;
        this.assignedTime = assignedTime;
        this.completionTime = completionTime;
        this.feedbackRating = feedbackRating;
        this.feedbackNotes = feedbackNotes;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public int getAssignedCaregiverId() {
        return assignedCaregiverId;
    }

    public void setAssignedCaregiverId(int assignedCaregiverId) {
        this.assignedCaregiverId = assignedCaregiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public LocalDateTime getAssignedTime() {
        return assignedTime;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public int getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(int feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackNotes() {
        return feedbackNotes;
    }

    public void setFeedbackNotes(String feedbackNotes) {
        this.feedbackNotes = feedbackNotes;
    }

    public void assignCaregiver(int caregiverId) {
        this.assignedCaregiverId = caregiverId;
        this.status = "ASSIGNED";
        this.assignedTime = LocalDateTime.now();
    }

    public void startWork() {
        this.status = "IN_PROGRESS";
    }

    public void complete() {
        this.status = "COMPLETED";
        this.completionTime = LocalDateTime.now();
    }

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }

    public boolean isValid() {
        return elderlyId > 0
                && serviceType != null && !serviceType.isEmpty()
                && urgency != null && !urgency.isEmpty();
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id=" + requestId +
                ", elderlyId=" + elderlyId +
                ", serviceType='" + serviceType + '\'' +
                ", urgency='" + urgency + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
