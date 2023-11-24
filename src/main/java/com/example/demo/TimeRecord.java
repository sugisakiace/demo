package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TimeRecord {
    private String employeeId;
    private String timeIn;
    private String timeOut;
    private String username;

    public TimeRecord(String employeeId, String timeIn, String timeOut, String username) {
        this.employeeId = employeeId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.username = username;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Static method to generate dummy time records for a specific user
    public static ObservableList<TimeRecord> generateDummyTimeRecords(String username) {
        ObservableList<TimeRecord> timeRecords = FXCollections.observableArrayList();

        // Generate some dummy time records with the provided username
        TimeRecord record1 = new TimeRecord("employee1", "8:00 AM", "4:00 PM", username);
        TimeRecord record2 = new TimeRecord("employee2", "9:00 AM", "5:00 PM", username);

        // Add the records to the list
        timeRecords.addAll(record1, record2);

        return timeRecords;
    }
}
