package com.nis.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceListRequest {
    private String search;
    private int page_number;
    private int page_size;
}
