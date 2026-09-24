package com.eldercare.models;

import java.time.LocalDateTime;

public class AuditLog {

    private int logId;
    private String tableName;
    private int recordId;
    private String action;
    private String oldValues;
    private String newValues;
    private String changedBy;
    private LocalDateTime changedTime;


    // Constructor for creating a new audit log
    public AuditLog(String tableName,
                    int recordId,
                    String action,
                    String oldValues,
                    String newValues,
                    String changedBy) {

        this.tableName = tableName;
        this.recordId = recordId;
        this.action = action;
        this.oldValues = oldValues;
        this.newValues = newValues;
        this.changedBy = changedBy;
        this.changedTime = LocalDateTime.now();
    }


    // Constructor for loading from database
    public AuditLog(int logId,
                    String tableName,
                    int recordId,
                    String action,
                    String oldValues,
                    String newValues,
                    String changedBy,
                    LocalDateTime changedTime) {

        this.logId = logId;
        this.tableName = tableName;
        this.recordId = recordId;
        this.action = action;
        this.oldValues = oldValues;
        this.newValues = newValues;
        this.changedBy = changedBy;
        this.changedTime = changedTime;
    }


    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public LocalDateTime getChangedTime() {
        return changedTime;
    }


    public boolean isValid() {
        return tableName != null
                && !tableName.isEmpty()
                && action != null
                && !action.isEmpty();
    }


    @Override
    public String toString() {
        return "AuditLog{" +
                "logId=" + logId +
                ", tableName='" + tableName + '\'' +
                ", recordId=" + recordId +
                ", action='" + action + '\'' +
                ", changedBy='" + changedBy + '\'' +
                ", changedTime=" + changedTime +
                '}';
    }
}
