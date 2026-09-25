package com.example.eldercareui;

import javafx.application.Application;
import javafx.stage.Stage;
import com.eldercare.service.ReminderEngine;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            ReminderEngine reminderEngine = new ReminderEngine();
            reminderEngine.startAutoCheck();
            System.out.println("✓ Reminder engine started");
        } catch (Exception e) {
            System.out.println("Note: Reminder engine unavailable");
        }

        LoginWindow loginWindow = new LoginWindow();
        loginWindow.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}