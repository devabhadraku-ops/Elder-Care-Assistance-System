package com.eldercare.models;

import java.time.LocalDateTime;

public class CaregiverTask {

    private int taskId;
    private int caregiverId;
    private int elderlyId;

    private String taskType;
    private String description;

    private LocalDateTime createdDate;
    private LocalDateTime dueDate;

    private String status;
    private LocalDateTime completionTime;


    // Constructor for creating a new task
    public CaregiverTask(int caregiverId,
                         int elderlyId,
                         String taskType,
                         String description,
                         LocalDateTime dueDate) {

        this.caregiverId = caregiverId;
        this.elderlyId = elderlyId;
        this.taskType = taskType;
        this.description = description;
        this.dueDate = dueDate;
        this.status = "PENDING";
        this.createdDate = LocalDateTime.now();
    }


    // Constructor for loading from database
    public CaregiverTask(int taskId,
                         int caregiverId,
                         int elderlyId,
                         String taskType,
                         String description,
                         LocalDateTime createdDate,
                         LocalDateTime dueDate,
                         String status,
                         LocalDateTime completionTime) {

        this.taskId = taskId;
        this.caregiverId = caregiverId;
        this.elderlyId = elderlyId;
        this.taskType = taskType;
        this.description = description;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.status = status;
        this.completionTime = completionTime;
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }


    public boolean isCompleted() {
        return "COMPLETED".equalsIgnoreCase(status);
    }


    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }


    public boolean isValid() {
        return caregiverId > 0
                && elderlyId > 0;
    }


    @Override
    public String toString() {
        return "CaregiverTask{" +
                "taskId=" + taskId +
                ", caregiverId=" + caregiverId +
                ", elderlyId=" + elderlyId +
                ", taskType='" + taskType + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                ", completionTime=" + completionTime +
                '}';
    }
}