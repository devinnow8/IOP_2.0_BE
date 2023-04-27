package com.nis.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {

    Pending("Pending"),Complete("Complete"),Cancelled("Cancelled"),Failed("Failed");

    private String status;

    PaymentStatus(String status) {
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
    public static PaymentStatus getStatusFromCode(final String value) {

        for (PaymentStatus status : PaymentStatus.values()) {

            if (status.getStatus().equals(value)) {
                return status;
            }
        }

        return null;
    }

}
