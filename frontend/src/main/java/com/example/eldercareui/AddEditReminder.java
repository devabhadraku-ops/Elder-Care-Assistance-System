package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddEditReminder extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Add/Edit Reminder");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label typeLabel = new Label("Activity Type:");
        typeLabel.setStyle("-fx-font-size: 14;");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Medication", "Meal", "Exercise", "Doctor Appointment", "Other");
        typeCombo.setValue("Medication");
        typeCombo.setPrefWidth(300);

        HBox typeBox = new HBox(15);
        typeBox.setPadding(new Insets(10));
        typeBox.getChildren().addAll(typeLabel, typeCombo);

        Label timeLabel = new Label("Time:");
        timeLabel.setStyle("-fx-font-size: 14;");
        TextField timeField = new TextField();
        timeField.setPromptText("HH:MM");
        timeField.setPrefWidth(300);
        timeField.setStyle("-fx-font-size: 14; -fx-padding: 10;");

        HBox timeBox = new HBox(15);
        timeBox.setPadding(new Insets(10));
        timeBox.getChildren().addAll(timeLabel, timeField);

        Label frequencyLabel = new Label("Frequency:");
        frequencyLabel.setStyle("-fx-font-size: 14;");

        CheckBox dailyCheck = new CheckBox("Daily");
        dailyCheck.setStyle("-fx-font-size: 14;");
        CheckBox weekdaysCheck = new CheckBox("Weekdays");
        weekdaysCheck.setStyle("-fx-font-size: 14;");
        CheckBox weekendCheck = new CheckBox("Weekend");
        weekendCheck.setStyle("-fx-font-size: 14;");

        HBox frequencyBox = new HBox(15);
        frequencyBox.setPadding(new Insets(10));
        frequencyBox.getChildren().addAll(frequencyLabel, dailyCheck, weekdaysCheck, weekendCheck);

        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-size: 14;");
        TextArea descriptionArea = new TextArea();
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
        saveButton.setOnAction(e -> System.out.println("Reminder saved"));

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

    public static void main(String[] args) {
        launch(args);
    }
}