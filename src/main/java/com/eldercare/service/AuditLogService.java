
package com.eldercare.service;

import com.eldercare.dao.AuditLogDAO;
import com.eldercare.models.AuditLog;

import java.util.List;

public class AuditLogService {

    private final AuditLogDAO auditLogDAO;

    public AuditLogService() {
        this.auditLogDAO = new AuditLogDAO();
    }


    // Add a new audit log
    public boolean addAuditLog(AuditLog auditLog) {

        if (auditLog == null || !auditLog.isValid()) {
            return false;
        }

        return auditLogDAO.add(auditLog);
    }


    // Get audit log by ID
    public AuditLog getAuditLogById(int logId) {

        if (logId <= 0) {
            return null;
        }

        return auditLogDAO.findById(logId);
    }


    // Get audit logs for a specific table
    public List<AuditLog> getAuditLogsByTableName(String tableName) {

        if (tableName == null || tableName.isBlank()) {
            return List.of();
        }

        return auditLogDAO.findByTableName(tableName);
    }


    // Get audit logs for a specific record
    public List<AuditLog> getAuditLogsByRecordId(int recordId) {

        if (recordId <= 0) {
            return List.of();
        }

        return auditLogDAO.findByRecordId(recordId);
    }


    // Get all audit logs
    public List<AuditLog> getAllAuditLogs() {

        return auditLogDAO.findAll();
    }


    // Delete an audit log
    public boolean deleteAuditLog(int logId) {

        if (logId <= 0) {
            return false;
        }

        return auditLogDAO.delete(logId);
    }
}