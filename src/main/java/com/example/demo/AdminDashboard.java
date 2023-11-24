package com.example.demo;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class AdminDashboard {
    private final Stage primaryStage;
    private final String username;
    private ObservableList<TimeRecord> timeRecords;
    private TableView<TimeRecord> tableView; // Declare tableView as an instance variable

    public AdminDashboard(Stage primaryStage, String username, ObservableList<TimeRecord> timeRecords) {
        this.primaryStage = primaryStage;
        this.username = username;
        this.timeRecords = timeRecords;
    }

    public void show() {
        primaryStage.setTitle("Admin Dashboard");

        // Create a table to display time records (similar to UserDashboard)
        tableView = createTimeRecordTable(); // Initialize tableView here

        // Enable multiple selection in the table view
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Create a VBox to layout the content
        VBox root = new VBox(10);
        root.getChildren().addAll(new Label("Welcome, " + username + "!"), createSignOutButton(), tableView);

        // Create a button to calculate payroll
        Button calculatePayrollButton = new Button("Calculate Payroll");
        calculatePayrollButton.setOnAction(e -> calculatePayroll());

        root.getChildren().add(calculatePayrollButton); // Add the button to the layout

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
    }

    private Button createSignOutButton() {
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(e -> {
            // Close the admin dashboard
            primaryStage.close();
            // Return to the login page
            new Main().start(new Stage());
        });
        return signOutButton;
    }

    private TableView<TimeRecord> createTimeRecordTable() {
        TableView<TimeRecord> table = new TableView<>();

        TableColumn<TimeRecord, String> employeeIdCol = new TableColumn<>("Employee ID");
        employeeIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        TableColumn<TimeRecord, String> timeInCol = new TableColumn<>("Time In");
        timeInCol.setCellValueFactory(new PropertyValueFactory<>("timeIn"));

        TableColumn<TimeRecord, String> timeOutCol = new TableColumn<>("Time Out");
        timeOutCol.setCellValueFactory(new PropertyValueFactory<>("timeOut"));

        table.setItems(timeRecords); // Use the 'timeRecords' provided as a parameter
        table.getColumns().addAll(employeeIdCol, timeInCol, timeOutCol);

        // Add double-click event handler to open an edit dialog
        table.setRowFactory(tv -> {
            TableRow<TimeRecord> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    TimeRecord selectedRecord = row.getItem();
                    editTimeRecord(selectedRecord);
                }
            });
            return row;
        });

        return table;
    }

    private void editTimeRecord(TimeRecord timeRecord) {
        // Open a dialog to edit the time record details
        Dialog<TimeRecord> dialog = new Dialog<>();
        dialog.setTitle("Edit Time Record");
        dialog.setHeaderText("Edit Time Record for Employee ID: " + timeRecord.getEmployeeId());

        // Create fields for editing (e.g., TextField for timeIn and timeOut)
        TextField timeInField = new TextField(timeRecord.getTimeIn());
        TextField timeOutField = new TextField(timeRecord.getTimeOut());

        // Add fields to the dialog
        GridPane grid = new GridPane();
        grid.add(new Label("Time In:"), 0, 0);
        grid.add(timeInField, 1, 0);
        grid.add(new Label("Time Out:"), 0, 1);
        grid.add(timeOutField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        // Create OK and Cancel buttons
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        // Convert the result to a TimeRecord when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                // Update the time record with the edited data
                timeRecord.setTimeIn(timeInField.getText());
                timeRecord.setTimeOut(timeOutField.getText());
                return timeRecord;
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait();

        // Update the table view to reflect the edited data
        tableView.refresh(); // This line will refresh the table view
    }

    private void calculatePayroll() {
        // Get the selected records from the table view
        ObservableList<TimeRecord> selectedRecords = tableView.getSelectionModel().getSelectedItems();

        if (selectedRecords.isEmpty()) {
            showAlert();
            return;
        }

        // Call the Calculate class to calculate payroll for the selected records
        Map<String, Double> payrollMap = Calculate.calculatePayroll(selectedRecords);

        // Display the payroll information using the showPayrollInformation method
        Calculate.showPayrollInformation(payrollMap);
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Records Selected");
        alert.setHeaderText(null);
        alert.setContentText("Please select one or more records to calculate payroll.");
        alert.showAndWait();
    }
}
