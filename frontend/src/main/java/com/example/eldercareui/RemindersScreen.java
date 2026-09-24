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
import java.util.List;

public class RemindersScreen extends Application {
    private int currentElderly Id = 1; // Default - set from login
    private ListView<String> remindersList;
    private List<ActivityReminder> remindersData; // Store reminder objects for mark complete

    public RemindersScreen() {
        this(1);
    }

    public RemindersScreen(int elderlyId) {
        this.currentElderly Id = elderlyId;
    }

    @Override
    public void start(Stage primaryStage) {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label screenTitle = new Label("Reminders");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label filterLabel = new Label("Filter:");
        filterLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All", "Completed", "Pending");
        filterCombo.setValue("All");
        filterCombo.setPrefWidth(180);
        filterCombo.setStyle("-fx-font-size: 13;");

        HBox filterBox = new HBox(15);
        filterBox.setPadding(new Insets(10));
        filterBox.getChildren().addAll(filterLabel, filterCombo);

        remindersList = new ListView<>();
        remindersList.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        remindersList.setPrefHeight(300);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setPadding(new Insets(15));

        Button completeButton = new Button("Mark Complete");
        completeButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        completeButton.setPrefWidth(150);
        completeButton.setPrefHeight(50);
        completeButton.setOnAction(e -> {
            String selected = remindersList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Please select a reminder");
                return;
            }

            try {
                int selectedIndex = remindersList.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < remindersData.size()) {
                    ActivityReminder reminder = remindersData.get(selectedIndex);
                    ReminderService service = new ReminderService();
                    service.markComplete(reminder.getReminder_id());

                    remindersList.getItems().remove(selected);
                    System.out.println("✓ Reminder marked complete");
                    showSuccess("Reminder marked complete!");
                }
            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                showError("Error: " + ex.getMessage());
            }
        });

        Button snoozeButton = new Button("Snooze 30 Min");
        snoozeButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        snoozeButton.setPrefWidth(150);
        snoozeButton.setPrefHeight(50);
        snoozeButton.setOnAction(e -> {
            String selected = remindersList.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Please select a reminder");
                return;
            }

            try {
                int selectedIndex = remindersList.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < remindersData.size()) {
                    ActivityReminder reminder = remindersData.get(selectedIndex);
                    ReminderService service = new ReminderService();
                    service.snoozeReminder(reminder.getReminder_id(), 30);

                    System.out.println("✓ Reminder snoozed for 30 minutes");
                    showSuccess("Reminder snoozed for 30 minutes!");
                }
            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                showError("Error: " + ex.getMessage());
            }
        });

        Button addButton = new Button("Add New");
        addButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        addButton.setPrefWidth(150);
        addButton.setPrefHeight(50);
        addButton.setOnAction(e -> System.out.println("Add reminder"));

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(50);
        backButton.setOnAction(e -> System.out.println("Back to dashboard"));

        buttonsBox.getChildren().addAll(completeButton, snoozeButton, addButton, backButton);

        mainContainer.getChildren().addAll(
                titleLabel,
                screenTitle,
                new Separator(),
                filterBox,
                remindersList,
                new Separator(),
                buttonsBox
        );

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 700);
        primaryStage.setTitle("Eldercare - Reminders");
        primaryStage.setScene(scene);
        primaryStage.show();

        // LOAD REMINDERS FROM DATABASE
        loadReminders();
    }

    // LOAD REMINDERS FROM DATABASE
    private void loadReminders() {
        try {
            ReminderService service = new ReminderService();
            remindersData = service.getRemindersByElderly Id(currentElderly Id);

            remindersList.getItems().clear();
            for (ActivityReminder reminder : remindersData) {
                String text = reminder.getActivityType() + " - " + reminder.getReminderTime();
                remindersList.getItems().add(text);
            }

            System.out.println("✓ Loaded " + remindersData.size() + " reminders");

        } catch (Exception e) {
            System.out.println("❌ Error loading reminders: " + e.getMessage());
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