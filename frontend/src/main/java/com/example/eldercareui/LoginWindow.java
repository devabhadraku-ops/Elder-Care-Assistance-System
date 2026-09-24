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

        Label userTypeLabel = new Label("User