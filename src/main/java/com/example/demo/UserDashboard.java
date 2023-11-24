package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;
import java.util.stream.Collectors;

public class UserDashboard {
    private Stage primaryStage;
    private String username;
    private ObservableList<TimeRecord> timeRecords;
    private TableView<TimeRecord> tableView; // Declare tableView as an instance variable

    public UserDashboard(Stage primaryStage, String username, ObservableList<TimeRecord> timeRecords) {
        this.primaryStage = primaryStage;
        this.username = username;
        this.timeRecords = timeRecords;
    }

    public void show() {
        primaryStage.setTitle("User Dashboard");

        // Filter timeRecords to include only records for "employee1"
        ObservableList<TimeRecord> employee1Records = filterRecordsForEmployee("employee1");

        // Create a table to display time records (similar to AdminDashboard)
        tableView = createTimeRecordTable(employee1Records); // Initialize tableView here

        // Enable multiple selection in the table view
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Create a VBox to layout the content
        VBox root = new VBox(10);
        root.getChildren().addAll(new Label("Welcome, User " + username + "!"), createSignOutButton(), tableView);

        // Create a button to calculate payroll (similar to AdminDashboard)
        Button calculatePayrollButton = new Button("Calculate Payroll");
        calculatePayrollButton.setOnAction(e -> calculatePayroll(employee1Records));

        root.getChildren().add(calculatePayrollButton); // Add the button to the layout

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
    }

    private Button createSignOutButton() {
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(e -> {
            // Close the user dashboard
            primaryStage.close();
            // Return to the login page
            new Main().start(new Stage());
        });
        return signOutButton;
    }

    private TableView<TimeRecord> createTimeRecordTable(ObservableList<TimeRecord> records) {
        TableView<TimeRecord> table = new TableView<>();

        TableColumn<TimeRecord, String> employeeIdCol = new TableColumn<>("Employee ID");
        employeeIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        TableColumn<TimeRecord, String> timeInCol = new TableColumn<>("Time In");
        timeInCol.setCellValueFactory(new PropertyValueFactory<>("timeIn"));

        TableColumn<TimeRecord, String> timeOutCol = new TableColumn<>("Time Out");
        timeOutCol.setCellValueFactory(new PropertyValueFactory<>("timeOut"));

        table.setItems(records); // Use the filtered records
        table.getColumns().addAll(employeeIdCol, timeInCol, timeOutCol);

        return table;
    }

    private ObservableList<TimeRecord> filterRecordsForEmployee(String employeeId) {
        return timeRecords.stream()
                .filter(record -> record.getEmployeeId().equals(employeeId))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    private void calculatePayroll(ObservableList<TimeRecord> records) {
        // Get the selected records from the table view (similar to AdminDashboard)
        ObservableList<TimeRecord> selectedRecords = tableView.getSelectionModel().getSelectedItems();

        if (selectedRecords.isEmpty()) {
            showAlert("No Records Selected", "Please select one or more records to calculate payroll.");
            return;
        }

        // Call the Calculate class to calculate payroll for the selected records
        Map<String, Double> payrollMap = Calculate.calculatePayroll(selectedRecords);

        // Display the payroll information using the showPayrollInformation method
        Calculate.showPayrollInformation(payrollMap);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
