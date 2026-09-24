package com.eldercare.service;

import com.eldercare.dao.CaregiverAssignmentDAO;
import com.eldercare.models.CaregiverAssignment;

import java.util.List;

public class CaregiverAssignmentService {

    private final CaregiverAssignmentDAO assignmentDAO;

    public CaregiverAssignmentService() {
        this.assignmentDAO = new CaregiverAssignmentDAO();
    }

    public boolean addAssignment(CaregiverAssignment assignment) {

        if (assignment == null || !assignment.isValid()) {
            return false;
        }

        return assignmentDAO.add(assignment);
    }

    public CaregiverAssignment getAssignmentById(int assignmentId) {

        if (assignmentId <= 0) {
            return null;
        }

        return assignmentDAO.findById(assignmentId);
    }

    public List<CaregiverAssignment> getAssignmentsByCaregiverId(int caregiverId) {

        if (caregiverId <= 0) {
            return List.of();
        }

        return assignmentDAO.findByCaregiverId(caregiverId);
    }

    public List<CaregiverAssignment> getAssignmentsByElderlyId(int elderlyId) {

        if (elderlyId <= 0) {
            return List.of();
        }

        return assignmentDAO.findByElderlyId(elderlyId);
    }

    public List<CaregiverAssignment> getActiveAssignments() {

        return assignmentDAO.findAllActive();
    }

    public boolean updateAssignment(CaregiverAssignment assignment) {

        if (assignment == null
                || assignment.getAssignmentId() <= 0
                || !assignment.isValid()) {
            return false;
        }

        return assignmentDAO.update(assignment);
    }

    public boolean deleteAssignment(int assignmentId) {

        if (assignmentId <= 0) {
            return false;
        }

        return assignmentDAO.delete(assignmentId);
    }
}