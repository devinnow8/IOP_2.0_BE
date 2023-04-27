package com.nis.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ErrorResponse {

    private LocalDate timestamp;
    private String message;
    private String details;
}
