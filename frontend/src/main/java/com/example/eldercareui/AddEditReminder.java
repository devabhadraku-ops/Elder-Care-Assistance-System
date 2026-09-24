package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.eldercare.service.ReminderService;
import com.eldercare.models.ActivityReminder;

public class AddEditReminder extends Application {
    private int currentElderly_id = 1; // Default - set from login
    private ComboBox<String> typeCombo;
    private TextField timeField;
    private CheckBox dailyCheck;
    private CheckBox weekdaysCheck;
    private CheckBox weekendCheck;
    private TextArea descriptionArea;

    public AddEditReminder() {
        this(1);
    }

    public AddEditReminder(int elderlyId) {
        this.currentElderly_id = elderlyId;
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Add/Edit Reminder");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label typeLabel = new Label("Activity Type:");
        typeLabel.setStyle("-fx-font-size: 14;");
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Medication", "Meal", "Exercise", "Doctor Appointment", "Other");
        typeCombo.setValue("Medication");
        typeCombo.setPrefWidth(300);

        HBox typeBox = new HBox(15);
        typeBox.setPadding(new Insets(10));
        typeBox.getChildren().addAll(typeLabel, typeCombo);

        Label timeLabel = new Label("Time:");
        timeLabel.setStyle("-fx-font-size: 14;");
        timeField = new TextField();
        timeField.setPromptText("HH:MM");
        timeField.setPrefWidth(300);
        timeField.setStyle("-fx-font-size: 14; -fx-padding: 10;");

        HBox timeBox = new HBox(15);
        timeBox.setPadding(new Insets(10));
        timeBox.getChildren().addAll(timeLabel, timeField);

        Label frequencyLabel = new Label("Frequency:");
        frequencyLabel.setStyle("-fx-font-size: 14;");

        dailyCheck = new CheckBox("Daily");
        dailyCheck.setStyle("-fx-font-size: 14;");
        weekdaysCheck = new CheckBox("Weekdays");
        weekdaysCheck.setStyle("-fx-font-size: 14;");
        weekendCheck = new CheckBox("Weekend");
        weekendCheck.setStyle("-fx-font-size: 14;");

        HBox frequencyBox = new HBox(15);
        frequencyBox.setPadding(new Insets(10));
        frequencyBox.getChildren().addAll(frequencyLabel, dailyCheck, weekdaysCheck, weekendCheck);

        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-size: 14;");
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter reminder details");
        descriptionArea.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        descriptionArea.setPrefHeight(150);
        descriptionArea.setWrapText(true);

        VBox descriptionBox = new VBox(10);
        descriptionBox.setPadding(new Insets(10));
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionArea);

        HBox actionButtonsBox = new HBox(15);
        actionButtonsBox.setPadding(new Insets(15));

        Button saveButton = new Button("Save Reminder");
        saveButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        saveButton.setPrefWidth(150);
        saveButton.setPrefHeight(50);
        saveButton.setOnAction(e -> saveReminder());

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(50);
        cancelButton.setOnAction(e -> System.out.println("Cancelled"));

        actionButtonsBox.getChildren().addAll(saveButton, cancelButton);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                screenTitle,
                new Separator(),
                typeBox,
                timeBox,
                frequencyBox,
                descriptionBox,
                new Separator(),
                actionButtonsBox
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 700, 800);
        primaryStage.setTitle("Eldercare - Add/Edit Reminder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // SAVE REMINDER TO DATABASE
    private void saveReminder() {
        try {
            // Validate inputs
            if (timeField.getText().isEmpty()) {
                showAlert("Please enter a time");
                return;
            }

            String activityType = typeCombo.getValue();
            String reminderTime = timeField.getText();
            String description = descriptionArea.getText();

            // Build frequency string
            String frequency = "";
            if (dailyCheck.isSelected()) frequency += "Daily ";
            if (weekdaysCheck.isSelected()) frequency += "Weekdays ";
            if (weekendCheck.isSelected()) frequency += "Weekend";
            if (frequency.isEmpty()) frequency = "Daily";

            // CREATE REMINDER SERVICE AND SAVE
            ReminderService service = new ReminderService();
            ActivityReminder reminder = service.createReminder(
                    currentElderly_id,
                    activityType,
                    reminderTime,
                    frequency,
                    description
            );

            if (reminder != null) {
                System.out.println("✓ Reminder saved with ID: " + reminder.getReminder_id());
                showSuccess("Reminder saved successfully!");
                // Clear form
                typeCombo.setValue("Medication");
                timeField.clear();
                descriptionArea.clear();
                dailyCheck.setSelected(false);
                weekdaysCheck.setSelected(false);
                weekendCheck.setSelected(false);
            } else {
                showError("Failed to save reminder");
            }

        } catch (Exception ex) {
            System.out.println("❌ Error: " + ex.getMessage());
            showError("Error saving reminder: " + ex.getMessage());
        }
    }

    // HELPER - Show Alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText("Attention");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // HELPER - Show Success
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Done");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // HELPER - Show Error
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}