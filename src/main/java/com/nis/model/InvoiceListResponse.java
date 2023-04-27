package com.nis.model;

import com.nis.entity.Invoice;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceListResponse {
    private List<Invoice> appointmentList;
    private int total_pages;
    private long total_count;
    private int page_number;
}
