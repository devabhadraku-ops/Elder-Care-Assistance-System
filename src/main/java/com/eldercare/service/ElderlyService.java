package com.eldercare.service;

import com.eldercare.dao.ElderlyDAO;
import com.eldercare.models.Elderly;

import java.util.List;

public class ElderlyService {

    private final ElderlyDAO elderlyDAO;

    public ElderlyService() {
        this.elderlyDAO = new ElderlyDAO();
    }

    // Add a new elderly person
    public boolean addElderly(Elderly elderly) {

        if (elderly == null) {
            return false;
        }

        if (!elderly.isValid()) {
            return false;
        }

        return elderlyDAO.add(elderly);
    }

    // Find elderly person by ID
    public Elderly getElderlyById(int id) {

        if (id <= 0) {
            return null;
        }

        return elderlyDAO.findById(id);
    }

    // Find elderly people by name
    public List<Elderly> searchByName(String name) {

        if (name == null || name.trim().isEmpty()) {
            return List.of();
        }

        return elderlyDAO.findByName(name.trim());
    }

    // Get all elderly people
    public List<Elderly> getAllElderly() {
        return elderlyDAO.findAll();
    }

    // Update elderly person
    public boolean updateElderly(Elderly elderly) {

        if (elderly == null) {
            return false;
        }

        if (elderly.getElderlyId() <= 0) {
            return false;
        }

        if (!elderly.isValid()) {
            return false;
        }

        return elderlyDAO.update(elderly);
    }

    // Delete elderly person
    public boolean deleteElderly(int id) {

        if (id <= 0) {
            return false;
        }

        return elderlyDAO.delete(id);
    }
}