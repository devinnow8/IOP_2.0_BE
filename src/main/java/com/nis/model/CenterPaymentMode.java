package com.nis.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CenterPaymentMode {

    Online("Online"),Offline("Offline");

    private String mode;

    CenterPaymentMode(String mode) {
        this.mode = mode;
    }

    @JsonValue
    String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return mode;
    }

    @JsonCreator
    public static CenterPaymentMode getModeFromCode(final String value) {

        for (CenterPaymentMode mode : CenterPaymentMode.values()) {

            if (mode.getMode().equals(value)) {
                return mode;
            }
        }

        return null;
    }

}
