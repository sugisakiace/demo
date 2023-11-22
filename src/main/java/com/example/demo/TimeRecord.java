package com.example.demo;

import javafx.beans.property.SimpleStringProperty;

public class TimeRecord {
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleStringProperty employeeId;

    public TimeRecord(String startTime, String endTime, String employeeId) {
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.employeeId = new SimpleStringProperty(employeeId);
    }

    // Getter and Setter for startTime
    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    // Getter and Setter for endTime
    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    // Getter and Setter for employeeId
    public String getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    // Property methods (optional, useful for binding and listeners in JavaFX)
    public SimpleStringProperty startTimeProperty() {
        return startTime;
    }

    public SimpleStringProperty endTimeProperty() {
        return endTime;
    }

    public SimpleStringProperty employeeIdProperty() {
        return employeeId;
    }
}
