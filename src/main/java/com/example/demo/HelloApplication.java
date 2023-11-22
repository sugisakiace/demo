package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class HelloApplication extends Application {

    private Label payrollLabel;


    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label scenetitle = new Label("Welcome to the Simple Login App");
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        grid.add(btn, 1, 3);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 4);

        btn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            if (username.equals("user") && password.equals("pass")) {
                showEmployeeDashboard();
            } else if (username.equals("admin") && password.equals("adminpass")) {
                showAdminDashboard();
            } else {
                actiontarget.setText("Invalid username or password.");
            }
        });


        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login App");
        primaryStage.show();
    }

    private ObservableList<TimeRecord> getTimeRecords() {
        ObservableList<TimeRecord> timeRecords = FXCollections.observableArrayList();

        // Dummy data with separate start and end times
        timeRecords.add(new TimeRecord("2023-11-21 08:00", "2023-11-21 16:00", "E001"));
        timeRecords.add(new TimeRecord("2023-11-22 08:00", "2023-11-22 16:00", "E002"));
        // ... add more records as needed

        return timeRecords;
    }


        private void showEmployeeDashboard () {
            Stage employeeStage = new Stage();
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
            Label welcomeLabel = new Label("Welcome to the Employee Dashboard");
            grid.add(welcomeLabel, 0, 0, 2, 1);

            payrollLabel = new Label();
            updatePayrollDisplay(getTimeRecords()); // Assuming you have dummy records

            grid.add(payrollLabel, 0, 2);
            Button computePayrollBtn = new Button("Compute Payroll");
            computePayrollBtn.setOnAction(e -> updatePayrollDisplay(getTimeRecords()));
            grid.add(computePayrollBtn, 0, 3);


            TableView<TimeRecord> table = new TableView<>();
            table.setEditable(false);

            TableColumn<TimeRecord, String> startTimeColumn = new TableColumn<>("Start Time");
            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

            TableColumn<TimeRecord, String> endTimeColumn = new TableColumn<>("End Time");
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

            // Add columns to the table
            table.getColumns().addAll(startTimeColumn, endTimeColumn);

            // Add data to the table (Assuming you have a method to get time records)
            table.setItems(getTimeRecords());

            grid.add(table, 0, 1);

            // Add other UI components relevant to employees
            // For example, a button to view schedule, a form to submit requests, etc.

            Scene scene = new Scene(grid, 400, 300);
            employeeStage.setScene(scene);
            employeeStage.setTitle("Employee Dashboard");
            employeeStage.show();
        }
        private void showAdminDashboard () {
            Stage adminStage = new Stage();
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Label welcomeLabel = new Label("Welcome to the Admin Dashboard");
            grid.add(welcomeLabel, 0, 0, 2, 1);

            TableView<TimeRecord> table = new TableView<>();
            table.setEditable(true); // Editable for admins

            payrollLabel = new Label("Total Payroll: PHP 0.00");
            grid.add(payrollLabel, 0, 2);

            Button computePayrollBtn = new Button("Compute Payroll");
            computePayrollBtn.setOnAction(e -> updatePayrollDisplay(getTimeRecords()));
            grid.add(computePayrollBtn, 0, 3);

            // Define columns for start time, end time, and other data
            TableColumn<TimeRecord, String> startTimeColumn = new TableColumn<>("Start Time");
            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));

            TableColumn<TimeRecord, String> endTimeColumn = new TableColumn<>("End Time");
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

            // Add columns to the table
            table.getColumns().addAll(startTimeColumn, endTimeColumn);

            // Add data to the table (Assuming you have a method to get time records)
            table.setItems(getTimeRecords());

            grid.add(table, 0, 1);

            // Add UI components for admin functionalities
            // For example, buttons for user management, report generation, system settings, etc.

            Scene scene = new Scene(grid, 400, 300);
            adminStage.setScene(scene);
            adminStage.setTitle("Admin Dashboard");
            adminStage.show();
        }

    public static void main(String[] args) {
        launch(args);
    }

    private void updatePayrollDisplay(ObservableList<TimeRecord> records) {
        double payroll = calculatePayroll(records);
        payrollLabel.setText("Total Payroll: PHP " + String.format("%.2f", payroll));
    }

    private double calculatePayroll(ObservableList<TimeRecord> timeRecords) {
        double hourlyRate = 500.0; // PHP 500 per hour
        double totalHours = 0.0;

        for (TimeRecord record : timeRecords) {
            try {
                String startTimeStr = record.getStartTime().split(" ")[1]; // Extract the time part
                String endTimeStr = record.getEndTime().split(" ")[1];     // Extract the time part

                LocalTime startTime = LocalTime.parse(startTimeStr);
                LocalTime endTime = LocalTime.parse(endTimeStr);

                Duration duration = Duration.between(startTime, endTime);
                totalHours += duration.toHours();
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing timestamp for employee "
                        + record.getEmployeeId() + ": " + e.getMessage());
            }
        }

        return totalHours * hourlyRate;
    }


    public class TimeRecord {
        private final SimpleStringProperty startTime;
        private final SimpleStringProperty endTime;
        private final SimpleStringProperty employeeId;

        public TimeRecord(String startTime, String endTime, String employeeId) {
            this.startTime = new SimpleStringProperty(startTime);
            this.endTime = new SimpleStringProperty(endTime);
            this.employeeId = new SimpleStringProperty(employeeId);
        }

        public String getStartTime() {
            return startTime.get();
        }

        public void setStartTime(String startTime) {
            this.startTime.set(startTime);
        }

        public String getEndTime() {
            return endTime.get();
        }

        public void setEndTime(String endTime) {
            this.endTime.set(endTime);
        }

        public String getEmployeeId() {
            return employeeId.get();
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId.set(employeeId);
        }
    }
}


