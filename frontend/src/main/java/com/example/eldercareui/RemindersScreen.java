package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemindersScreen extends Application {
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

        ListView<String> remindersList = new ListView<>();
        remindersList.getItems().addAll(
                "Take medication (Aspirin) - 9:00 AM",
                "Check blood pressure - 12:00 PM",
                "Lunch time - 1:00 PM",
                "Doctor appointment - 3:00 PM",
                "Evening walk - 5:00 PM"
        );
        remindersList.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        remindersList.setPrefHeight(300);

        HBox buttonsBox = new HBox(15);
        buttonsBox.setPadding(new Insets(15));

        Button completeButton = new Button("Mark Complete");
        completeButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        completeButton.setPrefWidth(150);
        completeButton.setPrefHeight(50);
        completeButton.setOnAction(e -> System.out.println("Reminder marked complete"));

        Button snoozeButton = new Button("Snooze 30 Min");
        snoozeButton.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        snoozeButton.setPrefWidth(150);
        snoozeButton.setPrefHeight(50);
        snoozeButton.setOnAction(e -> System.out.println("Reminder snoozed"));

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}