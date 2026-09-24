package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.eldercare.service.AuthService;
import com.eldercare.models.User;

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
                AuthService authService = new AuthService();
                User user = authService.login(username, password);

                if (user != null) {
                    showSuccess("Welcome " + user.getUsername() + "!");
                    System.out.println("✓ Login successful: " + user.getUsername() + " (ID: " + user.getUser_id() + ")");

                    int userId = user.getUser_id();
                    String role = user.getRole();

                    if ("elderly".equalsIgnoreCase(role)) {
                        ElderlyDashboard dashboard = new ElderlyDashboard(userId);
                        Stage dashboardStage = new Stage();
                        dashboard.start(dashboardStage);
                        primaryStage.close();
                    } else if ("caregiver".equalsIgnoreCase(role)) {
                        CaregiverDashboard dashboard = new CaregiverDashboard(userId);
                        Stage dashboardStage = new Stage();
                        dashboard.start(dashboardStage);
                        primaryStage.close();
                    } else {
                        showError("Unknown user role");
                    }

                } else {
                    showError("Invalid username or password");
                    System.out.println("❌ Login failed");
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