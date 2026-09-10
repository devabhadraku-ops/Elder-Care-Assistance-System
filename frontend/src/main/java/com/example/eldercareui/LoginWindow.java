package com.example.eldercareui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox mainContainer = new VBox(30);
        mainContainer.setPadding(new Insets(50));
        mainContainer.setStyle("-fx-background-color: #FFFFFF;");
        mainContainer.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("ELDERCARE ASSISTANCE SYSTEM");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Label subtitleLabel = new Label("Secure Login Portal");
        subtitleLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #666666;");

        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(40));
        formContainer.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 5;");
        formContainer.setPrefWidth(450);

        Label userTypeLabel = new Label("User Type:");
        userTypeLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #333333;");

        RadioButton elderlyRadio = new RadioButton("Elderly User");
        elderlyRadio.setStyle("-fx-font-size: 14;");
        RadioButton caregiverRadio = new RadioButton("Caregiver");
        caregiverRadio.setStyle("-fx-font-size: 14;");

        ToggleGroup group = new ToggleGroup();
        elderlyRadio.setToggleGroup(group);
        caregiverRadio.setToggleGroup(group);
        elderlyRadio.setSelected(true);

        VBox userTypeBox = new VBox(10);
        userTypeBox.getChildren().addAll(userTypeLabel, elderlyRadio, caregiverRadio);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #333333;");
        TextField usernameField = new TextField();
        usernameField.setStyle("-fx-font-size: 14; -fx-padding: 12;");
        usernameField.setPrefHeight(45);
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #333333;");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-font-size: 14; -fx-padding: 12;");
        passwordField.setPrefHeight(45);
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("LOGIN");
        loginButton.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-padding: 15; " +
                "-fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-cursor: hand;");
        loginButton.setPrefWidth(450);
        loginButton.setPrefHeight(50);
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String userType = elderlyRadio.isSelected() ? "Elderly" : "Caregiver";
            System.out.println("Login attempt: " + username + " (" + userType + ")");
        });

        formContainer.getChildren().addAll(
                userTypeBox,
                new Separator(),
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField
        );

        mainContainer.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                formContainer,
                loginButton
        );

        Scene scene = new Scene(mainContainer, 600, 700);
        primaryStage.setTitle("Eldercare - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}