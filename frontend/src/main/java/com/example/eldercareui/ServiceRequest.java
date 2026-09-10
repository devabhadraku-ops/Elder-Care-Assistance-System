package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServiceRequest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Service Request");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label serviceTypeLabel = new Label("Service Type:");
        serviceTypeLabel.setStyle("-fx-font-size: 14;");
        ComboBox<String> serviceTypeCombo = new ComboBox<>();
        serviceTypeCombo.getItems().addAll("Cleaning", "Cooking", "Transportation", "Medical Care", "Companionship", "Other");
        serviceTypeCombo.setValue("Cleaning");
        serviceTypeCombo.setPrefWidth(300);

        HBox serviceTypeBox = new HBox(15);
        serviceTypeBox.setPadding(new Insets(10));
        serviceTypeBox.getChildren().addAll(serviceTypeLabel, serviceTypeCombo);

        Label urgencyLabel = new Label("Urgency:");
        urgencyLabel.setStyle("-fx-font-size: 14;");

        RadioButton lowRadio = new RadioButton("Low");
        lowRadio.setStyle("-fx-font-size: 14;");
        RadioButton mediumRadio = new RadioButton("Medium");
        mediumRadio.setStyle("-fx-font-size: 14;");
        RadioButton highRadio = new RadioButton("High");
        highRadio.setStyle("-fx-font-size: 14;");

        ToggleGroup urgencyGroup = new ToggleGroup();
        lowRadio.setToggleGroup(urgencyGroup);
        mediumRadio.setToggleGroup(urgencyGroup);
        highRadio.setToggleGroup(urgencyGroup);
        mediumRadio.setSelected(true);

        HBox urgencyBox = new HBox(15);
        urgencyBox.setPadding(new Insets(10));
        urgencyBox.getChildren().addAll(urgencyLabel, lowRadio, mediumRadio, highRadio);

        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-size: 14;");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe the service needed");
        descriptionArea.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        descriptionArea.setPrefHeight(150);
        descriptionArea.setWrapText(true);

        VBox descriptionBox = new VBox(10);
        descriptionBox.setPadding(new Insets(10));
        descriptionBox.getChildren().addAll(descriptionLabel, descriptionArea);

        Label assignedCaregiverLabel = new Label("Assigned Caregiver Information");
        assignedCaregiverLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label caregiverNameLabel = new Label("Name: John Doe");
        caregiverNameLabel.setStyle("-fx-font-size: 14;");
        Label caregiverPhoneLabel = new Label("Phone: 555-1234");
        caregiverPhoneLabel.setStyle("-fx-font-size: 14;");
        Label caregiverStatusLabel = new Label("Status: Available");
        caregiverStatusLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #4CAF50;");

        VBox caregiverInfoBox = new VBox(10);
        caregiverInfoBox.setPadding(new Insets(15));
        caregiverInfoBox.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 1;");
        caregiverInfoBox.getChildren().addAll(caregiverNameLabel, caregiverPhoneLabel, caregiverStatusLabel);

        HBox actionButtonsBox = new HBox(15);
        actionButtonsBox.setPadding(new Insets(15));

        Button submitButton = new Button("Submit Request");
        submitButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        submitButton.setPrefWidth(150);
        submitButton.setPrefHeight(50);
        submitButton.setOnAction(e -> System.out.println("Service request submitted"));

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(50);
        cancelButton.setOnAction(e -> System.out.println("Request cancelled"));

        actionButtonsBox.getChildren().addAll(submitButton, cancelButton);

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                screenTitle,
                new Separator(),
                serviceTypeBox,
                urgencyBox,
                descriptionBox,
                new Separator(),
                assignedCaregiverLabel,
                caregiverInfoBox,
                new Separator(),
                actionButtonsBox
        );

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 800, 900);
        primaryStage.setTitle("Eldercare - Service Request");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}