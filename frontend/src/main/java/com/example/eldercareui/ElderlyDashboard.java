package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.eldercare.service.EmergencyService;
import com.eldercare.service.ReminderService;
import com.eldercare.service.AlertService;
import com.eldercare.models.SOSAlert;
import com.eldercare.models.ActivityReminder;
import com.eldercare.models.Alert;
import java.util.List;

public class ElderlyDashboard extends Application {
    private int currentElderly Id = 1; // Default elderly ID - will be set from login
    private ListView<String> remindersList;
    private ListView<String> alertsList;

    public ElderlyDashboard() {
        this(1); // Default constructor
    }

    public ElderlyDashboard(int elderlyId) {
        this.currentElderly Id = elderlyId;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label welcomeLabel = new Label("Welcome back!");
        welcomeLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Button sosButton = new Button("EMERGENCY - CALL NOW");
        sosButton.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-padding: 20; " +
                "-fx-background-color: #FF0000; -fx-text-fill: white;");
        sosButton.setPrefWidth(400);
        sosButton.setPrefHeight(80);

        // SOS BUTTON - CONNECTED TO BACKEND
        sosButton.setOnAction(e -> {
            try {
                EmergencyService service = new EmergencyService();
                SOSAlert alert = service.triggerSOS(currentElderly Id, "Emergency button pressed");

                if (alert != null) {
                    System.out.println("✓ SOS alert created with ID: " + alert.getAlert_id());

                    Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
                    confirmAlert.setTitle("Emergency Alert");
                    confirmAlert.setHeaderText("Alert Sent!");
                    confirmAlert.setContentText("Emergency alert has been sent to all caregivers!");
                    confirmAlert.showAndWait();
                }
            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Could not send alert");
                errorAlert.setContentText("Error: " + ex.getMessage());
                errorAlert.showAndWait();
            }
        });

        HBox sosBox = new HBox();
        sosBox.setAlignment(Pos.CENTER);
        sosBox.getChildren().add(sosButton);

        Label remindersTitle = new Label("Today's Reminders");
        remindersTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        remindersList = new ListView<>();
        remindersList.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        remindersList.setPrefHeight(150);

        Label alertsTitle = new Label("Active Alerts");
        alertsTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #FF9800;");

        alertsList = new ListView<>();
        alertsList.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        alertsList.setPrefHeight(80);

        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox actionsBox = new HBox(15);
        actionsBox.setPadding(new Insets(10));

        Button remindersButton = new Button("Reminders");
        remindersButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        remindersButton.setPrefWidth(140);
        remindersButton.setPrefHeight(50);

        Button healthButton = new Button("Health Log");
        healthButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        healthButton.setPrefWidth(140);
        healthButton.setPrefHeight(50);

        Button contactButton = new Button("Contact Caregiver");
        contactButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        contactButton.setPrefWidth(140);
        contactButton.setPrefHeight(50);

        Button settingsButton = new Button("Settings");
        settingsButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        settingsButton.setPrefWidth(140);
        settingsButton.setPrefHeight(50);

        actionsBox.getChildren().addAll(remindersButton, healthButton, contactButton, settingsButton);

        mainContainer.getChildren().addAll(
                titleLabel,
                welcomeLabel,
                new Separator(),
                sosBox,
                new Separator(),
                remindersTitle,
                remindersList,
                new Separator(),
                alertsTitle,
                alertsList,
                new Separator(),
                actionsTitle,
                actionsBox
        );

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 700);
        primaryStage.setTitle("Eldercare - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();

        // LOAD DATA FROM DATABASE
        loadData();
    }

    // LOAD REMINDERS FROM DATABASE
    private void loadReminders() {
        try {
            ReminderService service = new ReminderService();
            List<ActivityReminder> reminders = service.getRemindersByElderly Id(currentElderly Id);

            remindersList.getItems().clear();
            for (ActivityReminder reminder : reminders) {
                String text = reminder.getActivityType() + " - " + reminder.getReminderTime();
                remindersList.getItems().add(text);
            }

            System.out.println("✓ Loaded " + reminders.size() + " reminders");

        } catch (Exception e) {
            System.out.println("❌ Error loading reminders: " + e.getMessage());
        }
    }

    // LOAD ALERTS FROM DATABASE
    private void loadAlerts() {
        try {
            AlertService service = new AlertService();
            List<Alert> alerts = service.getAlertsByElderly Id(currentElderly Id);

            alertsList.getItems().clear();
            for (Alert alert : alerts) {
                String text = alert.getAlertType() + ": " + alert.getDescription();
                alertsList.getItems().add(text);
            }

            System.out.println("✓ Loaded " + alerts.size() + " alerts");

        } catch (Exception e) {
            System.out.println("❌ Error loading alerts: " + e.getMessage());
        }
    }

    // LOAD ALL DATA
    private void loadData() {
        loadReminders();
        loadAlerts();
    }

    public static void main(String[] args) {
        launch(args);
    }
}