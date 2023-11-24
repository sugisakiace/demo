package com.example.demo;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculate {
    // Hourly rate (in PHP)
    private static final double HOURLY_RATE = 62.5;

    public static Map<String, Double> calculatePayroll(List<TimeRecord> selectedRecords) {
        Map<String, Double> payrollMap = new HashMap<>();

        // Calculate payroll for each selected employee
        for (TimeRecord timeRecord : selectedRecords) {
            String employeeId = timeRecord.getEmployeeId();
            double totalHoursWorked = calculateTotalHoursWorked(timeRecord);
            double payroll = totalHoursWorked * HOURLY_RATE;

            // Store the payroll amount in the map
            payrollMap.put(employeeId, payroll);
        }

        return payrollMap;
    }


    private static double calculateTotalHoursWorked(TimeRecord timeRecord) {
        // Calculate total hours worked based on time-in and time-out
        String timeIn = timeRecord.getTimeIn();
        String timeOut = timeRecord.getTimeOut();

        // Split time strings into hours, minutes, and AM/PM parts
        String[] timeInParts = timeIn.split(" ");
        String[] timeOutParts = timeOut.split(" ");

        String timeInHoursMinutes = timeInParts[0];
        String timeOutHoursMinutes = timeOutParts[0];

        // Extract hours and minutes from time strings
        int timeInHours = Integer.parseInt(timeInHoursMinutes.split(":")[0]);
        int timeInMinutes = Integer.parseInt(timeInHoursMinutes.split(":")[1]);
        int timeOutHours = Integer.parseInt(timeOutHoursMinutes.split(":")[0]);
        int timeOutMinutes = Integer.parseInt(timeOutHoursMinutes.split(":")[1]);

        // Adjust for 12-hour time format (AM/PM)
        if (timeInParts.length > 1 && timeInParts[1].equalsIgnoreCase("PM") && timeInHours != 12) {
            timeInHours += 12;
        }
        if (timeOutParts.length > 1 && timeOutParts[1].equalsIgnoreCase("PM") && timeOutHours != 12) {
            timeOutHours += 12;
        }

        // Calculate total hours worked (assuming 60 minutes per hour)
        double totalHoursWorked = (timeOutHours - timeInHours) + (timeOutMinutes - timeInMinutes) / 60.0;

        return totalHoursWorked;
    }


    public static void showPayrollInformation(Map<String, Double> payrollMap) {
        // Create a TextArea to display the payroll information
        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        // Populate the TextArea with payroll information
        StringBuilder payrollInfo = new StringBuilder("Payroll Information:\n");
        for (Map.Entry<String, Double> entry : payrollMap.entrySet()) {
            payrollInfo.append("Employee ID: ").append(entry.getKey()).append(", Payroll: ₱").append(entry.getValue()).append("\n");
        }
        textArea.setText(payrollInfo.toString());

        // Create an Alert dialog to display the payroll information
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payroll Calculation");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }
}
