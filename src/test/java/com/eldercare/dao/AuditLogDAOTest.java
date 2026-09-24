package com.eldercare.dao;

import com.eldercare.models.AuditLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditLogDAOTest {

    @Test
    void testAddAndFindAuditLog() {

        AuditLogDAO auditLogDAO = new AuditLogDAO();

        AuditLog auditLog = new AuditLog(
                "elderly",
                1,
                "INSERT",
                null,
                "{\"name\":\"Test Elderly\"}",
                "test-user"
        );

        assertTrue(auditLogDAO.add(auditLog));
        assertTrue(auditLog.getLogId() > 0);

        AuditLog found =
                auditLogDAO.findById(auditLog.getLogId());

        assertNotNull(found);
        assertEquals("elderly", found.getTableName());
        assertEquals(1, found.getRecordId());
        assertEquals("INSERT", found.getAction());
        assertEquals(null, found.getOldValues());
        assertEquals("{\"name\":\"Test Elderly\"}", found.getNewValues());
        assertEquals("test-user", found.getChangedBy());

        auditLogDAO.delete(auditLog.getLogId());
    }
}
