package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.eldercare.service.ServiceRequestService;
import com.eldercare.models.ServiceRequest;

public class ServiceRequestScreen extends Application {
    private int currentElderly_id = 1; // Default - set from login
    private ComboBox<String> serviceTypeCombo;
    private RadioButton lowRadio;
    private RadioButton mediumRadio;
    private RadioButton highRadio;
    private TextArea descriptionArea;

    public ServiceRequestScreen() {
        this(1);
    }

    public ServiceRequestScreen(int elderlyId) {
        this.currentElderly_id = elderlyId;
    }

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Label screenTitle = new Label("Service Request");
        screenTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label serviceTypeLabel = new Label("Service Type:");
        serviceTypeLabel.setStyle("-fx-font-size: 14;");
        serviceTypeCombo = new ComboBox<>();
        serviceTypeCombo.getItems().addAll("Cleaning", "Cooking", "Transportation", "Medical Care", "Companionship", "Other");
        serviceTypeCombo.setValue("Cleaning");
        serviceTypeCombo.setPrefWidth(300);

        HBox serviceTypeBox = new HBox(15);
        serviceTypeBox.setPadding(new Insets(10));
        serviceTypeBox.getChildren().addAll(serviceTypeLabel, serviceTypeCombo);

        Label urgencyLabel = new Label("Urgency:");
        urgencyLabel.setStyle("-fx-font-size: 14;");

        lowRadio = new RadioButton("Low");
        lowRadio.setStyle("-fx-font-size: 14;");
        mediumRadio = new RadioButton("Medium");
        mediumRadio.setStyle("-fx-font-size: 14;");
        highRadio = new RadioButton("High");
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
        descriptionArea = new TextArea();
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
        submitButton.setOnAction(e -> submitRequest());

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

    // SUBMIT SERVICE REQUEST TO DATABASE
    private void submitRequest() {
        try {
            // Validate inputs
            if (descriptionArea.getText().isEmpty()) {
                showAlert("Please enter a description");
                return;
            }

            String serviceType = serviceTypeCombo.getValue();
            String description = descriptionArea.getText();

            // Get urgency level
            String urgency = "Medium"; // default
            if (lowRadio.isSelected()) urgency = "Low";
            else if (mediumRadio.isSelected()) urgency = "Medium";
            else if (highRadio.isSelected()) urgency = "High";

            // CREATE SERVICE REQUEST AND SAVE
            ServiceRequestService service = new ServiceRequestService();
            ServiceRequest request = service.createRequest(
                    currentElderly_id,
                    serviceType,
                    urgency,
                    description
            );

            if (request != null) {
                System.out.println("✓ Service request saved with ID: " + request.getRequest_id());
                showSuccess("Service request submitted successfully!");
                // Clear form
                serviceTypeCombo.setValue("Cleaning");
                descriptionArea.clear();
                mediumRadio.setSelected(true);
            } else {
                showError("Failed to submit service request");
            }

        } catch (Exception ex) {
            System.out.println("❌ Error: " + ex.getMessage());
            showError("Error submitting request: " + ex.getMessage());
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