package com.nis.model.dto;

import lombok.Data;

@Data
public class InvoiceDTO {

    private String invoice_id;
    private String merchant;
    private String merchant_service;
    private String applicant_first_name;
    private String applicant_last_name;
    private String applicant_phone_number;
    private String applicant_email;
    private String applicant_nationality;
    private String processing_center;
    private String processing_country;
    private float service_fee;
    private float gateway_fee;
    private float total;
    private String currency;
    private String mode_of_payment;
}
