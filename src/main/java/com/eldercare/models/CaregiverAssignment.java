package com.eldercare.models;

import java.time.LocalDate;

public class CaregiverAssignment {

    private int assignmentId;
    private int caregiverId;
    private int elderlyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignmentReason;
    private String assignmentStatus;
    private int hoursPerWeek;

    public CaregiverAssignment(int caregiverId, int elderlyId,
                               LocalDate startDate,
                               String assignmentReason,
                               int hoursPerWeek) {

        this.caregiverId = caregiverId;
        this.elderlyId = elderlyId;
        this.startDate = startDate;
        this.assignmentReason = assignmentReason;
        this.assignmentStatus = "ACTIVE";
        this.hoursPerWeek = hoursPerWeek;
    }

    public CaregiverAssignment(int assignmentId,
                               int caregiverId,
                               int elderlyId,
                               LocalDate startDate,
                               LocalDate endDate,
                               String assignmentReason,
                               String assignmentStatus,
                               int hoursPerWeek) {

        this.assignmentId = assignmentId;
        this.caregiverId = caregiverId;
        this.elderlyId = elderlyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignmentReason = assignmentReason;
        this.assignmentStatus = assignmentStatus;
        this.hoursPerWeek = hoursPerWeek;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(int caregiverId) {
        this.caregiverId = caregiverId;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getAssignmentReason() {
        return assignmentReason;
    }

    public void setAssignmentReason(String assignmentReason) {
        this.assignmentReason = assignmentReason;
    }

    public String getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(assignmentStatus);
    }

    public boolean isValid() {
        return caregiverId > 0
                && elderlyId > 0
                && startDate != null
                && hoursPerWeek >= 0;
    }

    @Override
    public String toString() {
        return "CaregiverAssignment{" +
                "assignmentId=" + assignmentId +
                ", caregiverId=" + caregiverId +
                ", elderlyId=" + elderlyId +
                ", startDate=" + startDate +
                ", status='" + assignmentStatus + '\'' +
                ", hoursPerWeek=" + hoursPerWeek +
                '}';
    }
}