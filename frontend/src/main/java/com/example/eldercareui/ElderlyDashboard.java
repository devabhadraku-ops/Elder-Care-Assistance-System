package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ElderlyDashboard extends Application {
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
        sosButton.setOnAction(e -> System.out.println("Emergency alert triggered"));

        HBox sosBox = new HBox();
        sosBox.setAlignment(Pos.CENTER);
        sosBox.getChildren().add(sosButton);

        Label remindersTitle = new Label("Today's Reminders");
        remindersTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #333333;");

        ListView<String> remindersList = new ListView<>();
        remindersList.getItems().addAll(
                "Take medication (Aspirin) - 9:00 AM",
                "Check blood pressure - 12:00 PM",
                "Lunch time - 1:00 PM",
                "Doctor appointment - 3:00 PM",
                "Evening activity - 5:00 PM"
        );
        remindersList.setStyle("-fx-font-size: 13; -fx-padding: 10;");
        remindersList.setPrefHeight(150);

        Label alertsTitle = new Label("Active Alerts");
        alertsTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #FF9800;");

        ListView<String> alertsList = new ListView<>();
        alertsList.getItems().addAll(
                "Medication reminder: Take vitamin D",
                "High blood pressure detected - Contact doctor"
        );
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}