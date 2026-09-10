package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CaregiverDashboard extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Caregiver Dashboard");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label urgentAlertsTitle = new Label("Urgent Alerts");
        urgentAlertsTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #FF0000;");

        ListView<String> alertsList = new ListView<>();
        alertsList.getItems().addAll(
                "High blood pressure alert - Patient: John Smith",
                "Medication overdue - Patient: Mary Johnson",
                "Fall detected - Patient: Robert Brown"
        );
        alertsList.setStyle("-fx-font-size: 14; -fx-padding: 10; -fx-border-color: #FF0000; -fx-border-width: 2;");
        alertsList.setPrefHeight(150);

        Label pendingTasksTitle = new Label("Pending Tasks");
        pendingTasksTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333333;");

        ListView<String> tasksList = new ListView<>();
        tasksList.getItems().addAll(
                "Assist with medication - John Smith - 2:00 PM",
                "Check vitals - Mary Johnson - 3:00 PM",
                "Prepare meal - Robert Brown - 4:00 PM",
                "Help with mobility - Sarah Davis - 5:00 PM"
        );
        tasksList.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        tasksList.setPrefHeight(200);

        Label assignedElderlyTitle = new Label("Assigned Elderly");
        assignedElderlyTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox elderlyBox = new HBox(15);
        elderlyBox.setPadding(new Insets(10));

        Button patient1Button = new Button("John Smith");
        patient1Button.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        patient1Button.setPrefWidth(150);
        patient1Button.setPrefHeight(60);

        Button patient2Button = new Button("Mary Johnson");
        patient2Button.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        patient2Button.setPrefWidth(150);
        patient2Button.setPrefHeight(60);

        Button patient3Button = new Button("Robert Brown");
        patient3Button.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        patient3Button.setPrefWidth(150);
        patient3Button.setPrefHeight(60);

        elderlyBox.getChildren().addAll(patient1Button, patient2Button, patient3Button);

        HBox actionButtonsBox = new HBox(15);
        actionButtonsBox.setPadding(new Insets(15));

        Button completeTaskButton = new Button("Mark Task Complete");
        completeTaskButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        completeTaskButton.setPrefWidth(150);
        completeTaskButton.setPrefHeight(50);
        completeTaskButton.setOnAction(e -> System.out.println("Task marked complete"));

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}