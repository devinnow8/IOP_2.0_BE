package com.nis.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum AppointmentStatus {

    Pending("Pending"),Complete("Complete"),Reschedule("Reschedule"),Cancel("Cancel");

    private String status;

    AppointmentStatus(String status) {
        this.status = status;
    }

    @JsonValue
    String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    @JsonCreator
    public static AppointmentStatus getStatusFromCode(final String value) {

        for (AppointmentStatus status : AppointmentStatus.values()) {

            if (status.getStatus().equals(value)) {
                return status;
            }
        }

        return null;
    }

}
