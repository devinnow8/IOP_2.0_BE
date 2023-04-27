package com.nis.model;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceResponse {

    private String invoice_id;
    private String order_id;
    private String gateway;
}
