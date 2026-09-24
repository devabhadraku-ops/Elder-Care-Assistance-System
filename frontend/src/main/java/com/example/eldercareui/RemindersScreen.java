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
    private int currentElderly_id = 1;
    private ListView<String> remindersList;
    private List<ActivityReminder> remindersData;

    public RemindersScreen() {
        this(1);
    }

    public RemindersScreen(int elderlyId) {
        this.currentElderly_id = elderlyId;
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Reminders");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        remindersList = new ListView<>();
        remindersList.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        remindersList.setPrefHeight(300);

        HBox actionButtonsBox = new HBox(15);
        actionButtonsBox.setPadding(new Insets(15));

        Button markCompleteButton = new Button("Mark Complete");
        markCompleteButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        markCompleteButton.setPrefWidth(150);
        markCompleteButton.setPrefHeight(50);
        markCompleteButton.setOnAction(e -> {
            int selectedIndex = remindersList.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                showAlert("Please select a reminder");
                return;
            }

            try {
                ActivityReminder reminder = remindersData.get(selectedIndex);
                ReminderService service = new ReminderService();
                boolean success = service.completeReminder(reminder.getReminder_id());

                if (success) {
                    remindersList.getItems().remove(selectedIndex);
                    showSuccess("Reminder marked complete!");
                    System.out.println("✓ Reminder marked complete");
                } else {
                    showError("Failed to mark reminder complete");
                }
            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                showError("Error: " + ex.getMessage());
            }
        });

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(50);
        backButton.setOnAction(e -> System.out.println("Going back"));

        actionButtonsBox.getChildren().addAll(markCompleteButton, backButton);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                screenTitle,
                new Separator(),
                remindersList,
                new Separator(),
                actionButtonsBox
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 700, 800);
        primaryStage.setTitle("Eldercare - Reminders");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadReminders();
    }

    private void loadReminders() {
        try {
            ReminderService service = new ReminderService();
            remindersData = service.getRemindersByElderlyId(currentElderly_id);

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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setHeaderText("Attention");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Done");
        alert.setContentText(message);
        alert.showAndWait();
    }

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