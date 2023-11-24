package com.example.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
    // User and admin credentials
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "user123";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("User Login");

        // Create form elements
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Create radio buttons to select user or admin login
        ToggleGroup userTypeToggleGroup = new ToggleGroup();
        RadioButton userRadioButton = new RadioButton("User");
        userRadioButton.setToggleGroup(userTypeToggleGroup);
        userRadioButton.setSelected(true); // Default to user login
        RadioButton adminRadioButton = new RadioButton("Admin");
        adminRadioButton.setToggleGroup(userTypeToggleGroup);

        // Create layout
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, userRadioButton, adminRadioButton, loginButton);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        // Create dummy time records for a specific user
        ObservableList<TimeRecord> timeRecords = TimeRecord.generateDummyTimeRecords("user"); // Initialize with dummy data for "user"

        // Create action for login button
        loginButton.setOnAction(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            if (userRadioButton.isSelected() && isValidUser(enteredUsername, enteredPassword, USER_USERNAME, USER_PASSWORD)) {
                // Successful user login
                showUserDashboard(enteredUsername, timeRecords);
            } else if (adminRadioButton.isSelected() && isValidUser(enteredUsername, enteredPassword, ADMIN_USERNAME, ADMIN_PASSWORD)) {
                // Successful admin login
                showAdminDashboard(enteredUsername, timeRecords); // Provide timeRecords
            } else {
                // Invalid login
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        // Add event listener to password field to trigger login on Enter key press
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginButton.fire(); // Trigger the login button's action
            }
        });

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isValidUser(String enteredUsername, String enteredPassword, String correctUsername, String correctPassword) {
        return enteredUsername.equals(correctUsername) && enteredPassword.equals(correctPassword);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showUserDashboard(String username, ObservableList<TimeRecord> timeRecords) {
        // Create and show the user dashboard
        UserDashboard userDashboard = new UserDashboard(primaryStage, username, timeRecords);
        userDashboard.show();
    }

    private void showAdminDashboard(String username, ObservableList<TimeRecord> timeRecords) {
        // Create and show the admin dashboard with the timeRecords list
        AdminDashboard adminDashboard = new AdminDashboard(primaryStage, username, timeRecords);
        adminDashboard.show();
    }
}
