package com.example.eldercareui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.eldercare.service.ReminderEngine;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // START REMINDER ENGINE IN BACKGROUND
        ReminderEngine reminderEngine = new ReminderEngine();
        reminderEngine.startAutoCheck();
        System.out.println("✓ Reminder engine started in background");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Eldercare Assistance System");
        stage.setScene(scene);
        stage.show();
    }