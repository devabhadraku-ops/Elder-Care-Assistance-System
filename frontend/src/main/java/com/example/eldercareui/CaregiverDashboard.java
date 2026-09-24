package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.eldercare.service.AlertService;
import com.eldercare.service.CaregiverAssignmentService;
import com.eldercare.models.Alert;
import com.eldercare.models.CaregiverAssignment;
import java.util.List;

public class CaregiverDashboard extends Application {
    private int currentCaregiver Id = 1; // Default - set from login
    private ListView<String> alertsList;
    private ListView<String> tasksList;
    private HBox elderlyBox;

    public CaregiverDashboard() {
        this(1);
    }

    public CaregiverDashboard(int caregiverId) {
        this.currentCaregiver Id = caregiverId;
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Caregiver Dashboard");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label urgentAlertsTitle = new Label("Urgent Alerts");
        urgentAlertsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #FF0000;");

        alertsList = new ListView<>();
        alertsList.setStyle("-fx-font-size: 14; -fx-padding: 10; -fx-border-color: #FF0000; -fx-border-width: 2;");
        alertsList.setPrefHeight(150);

        Label pendingTasksTitle = new Label("Pending Tasks");
        pendingTasksTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333333;");

        tasksList = new ListView<>();
        tasksList.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        tasksList.setPrefHeight(200);

        Label assignedElderlyTitle = new Label("Assigned Elderly");
        assignedElderlyTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333333;");

        elderlyBox = new HBox(15);
        elderlyBox.setPadding(new Insets(10));

        HBox actionButtonsBox = new HBox(15);
        actionButtonsBox.setPadding(new Insets(15));

        Button completeTaskButton = new Button("Mark Task Complete");
        completeTaskButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        completeTaskButton.setPrefWidth(150);
        completeTaskButton.setPrefHeight(50);
        completeTaskButton.setOnAction(e -> {
            String selected = tasksList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Please select a task");
                return;
            }

            try {
                tasksList.getItems().remove(selected);
                System.out.println("✓ Task marked complete");
                showSuccess("Task marked complete!");
            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                showError("Error: " + ex.getMessage());
            }
        });

        Button viewDetailsButton = new Button("View Details");
        viewDetailsButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        viewDetailsButton.setPrefWidth(150);
        viewDetailsButton.setPrefHeight(50);
        viewDetailsButton.setOnAction(e -> System.out.println("Viewing patient details"));

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(50);
        backButton.setOnAction(e -> System.out.println("Going back"));

        actionButtonsBox.getChildren().addAll(completeTaskButton, viewDetailsButton, backButton);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                screenTitle,
                new Separator(),
                urgentAlertsTitle,
                alertsList,
                new Separator(),
                pendingTasksTitle,
                tasksList,
                new Separator(),
                assignedElderlyTitle,
                elderlyBox,
                new Separator(),
                actionButtonsBox
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 900, 800);
        primaryStage.setTitle("Eldercare - Caregiver Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();

        // LOAD DATA FROM DATABASE
        loadData();
    }

    // LOAD ALERTS FROM DATABASE
    private void loadAlerts() {
        try {
            AlertService service = new AlertService();
            List<Alert> alerts = service.getUnacknowledgedAlerts();

            alertsList.getItems().clear();
            for (Alert alert : alerts) {
                String text = alert.getAlertType() + " - " + alert.getDescription();
                alertsList.getItems().add(text);
            }

            System.out.println("✓ Loaded " + alerts.size() + " alerts");

        } catch (Exception e) {
            System.out.println("❌ Error loading alerts: " + e.getMessage());
        }
    }

    // LOAD ASSIGNMENTS FROM DATABASE
    private void loadAssignments() {
        try {
            CaregiverAssignmentService service = new CaregiverAssignmentService();
            List<CaregiverAssignment> assignments = service.getAssignmentsByCaregiverId(currentCaregiver Id);

            elderlyBox.getChildren().clear();
            for (CaregiverAssignment assignment : assignments) {
                Button patientButton = new Button("Elderly ID: " + assignment.getElderly_id());
                patientButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
                patientButton.setPrefWidth(150);
                patientButton.setPrefHeight(60);
                elderlyBox.getChildren().add(patientButton);
            }

            System.out.println("✓ Loaded " + assignments.size() + " assignments");

        } catch (Exception e) {
            System.out.println("❌ Error loading assignments: " + e.getMessage());
        }
    }

    // LOAD ALL DATA
    private void loadData() {
        loadAlerts();
        loadAssignments();
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