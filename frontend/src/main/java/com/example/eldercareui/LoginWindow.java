package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.eldercare.service.AuthService;
import com.eldercare.models.Elderly;
import com.eldercare.models.Caregiver;

public class LoginWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label userTypeLabel = new Label("User Type:");
        userTypeLabel.setStyle("-fx-font-size: 14;");

        RadioButton elderlyRadio = new RadioButton("Elderly");
        elderlyRadio.setStyle("-fx-font-size: 14;");
        elderlyRadio.setSelected(true);

        RadioButton caregiverRadio = new RadioButton("Caregiver");
        caregiverRadio.setStyle("-fx-font-size: 14;");

        ToggleGroup userTypeGroup = new ToggleGroup();
        elderlyRadio.setToggleGroup(userTypeGroup);
        caregiverRadio.setToggleGroup(userTypeGroup);

        VBox userTypeBox = new VBox(10);
        userTypeBox.getChildren().addAll(userTypeLabel, elderlyRadio, caregiverRadio);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        usernameField.setPrefWidth(300);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        passwordField.setPrefWidth(300);

        Button loginButton = new Button("LOGIN");
        loginButton.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-padding: 15; " +
                "-fx-background-color: #2196F3; -fx-text-fill: white;");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(50);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showError("Please enter username and password");
                return;
            }

            try {
                boolean isElderly = elderlyRadio.isSelected();

                if (isElderly) {
                    AuthService authService = new AuthService();
                    Elderly elderly = authService.authenticateElderly(username, password);

                    if (elderly != null) {
                        showSuccess("Welcome " + elderly.getName() + "!");
                        System.out.println("✓ Elderly login successful: " + elderly.getName() + " (ID: " + elderly.getElderly_id() + ")");

                        ElderlyDashboard dashboard = new ElderlyDashboard(elderly.getElderly_id());
                        Stage dashboardStage = new Stage();
                        dashboard.start(dashboardStage);
                        primaryStage.close();

                    } else {
                        showError("Invalid username or password");
                        System.out.println("❌ Elderly login failed");
                    }

                } else {
                    AuthService authService = new AuthService();
                    Caregiver caregiver = authService.authenticateCaregiver(username, password);

                    if (caregiver != null) {
                        showSuccess("Welcome " + caregiver.getName() + "!");
                        System.out.println("✓ Caregiver login successful: " + caregiver.getName() + " (ID: " + caregiver.getCaregiver_id() + ")");

                        CaregiverDashboard dashboard = new CaregiverDashboard(caregiver.getCaregiver_id());
                        Stage dashboardStage = new Stage();
                        dashboard.start(dashboardStage);
                        primaryStage.close();

                    } else {
                        showError("Invalid username or password");
                        System.out.println("❌ Caregiver login failed");
                    }
                }

            } catch (Exception ex) {
                System.out.println("❌ Error: " + ex.getMessage());
                showError("Login error: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                loginLabel,
                userTypeBox,
                new Separator(),
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                new Separator(),
                loginButton
        );

        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("Eldercare - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Login Successful");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}