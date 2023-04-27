package com.nis.entity;

import com.nis.model.AppointmentStatus;
import com.nis.model.PaymentStatus;
import com.nis.repository.InvoiceIdGenerator;
import lombok.Data;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
//@Table(schema = "${spring.jpa.properties.hibernate.default_schema}")
public class Invoice {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
    @GenericGenerator(
            name = "invoice_id_seq",
            strategy = "com.nis.repository.InvoiceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = InvoiceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = InvoiceIdGenerator.VALUE_PREFIX_PARAMETER, value = "INV-"),
                    @org.hibernate.annotations.Parameter(name = InvoiceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d") })
    @Column(name = "invoice_id")
    private String invoiceId;
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
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status=PaymentStatus.Pending;
    private Long payment_id;
    @CreationTimestamp
    private LocalDateTime created_date;
    @CreationTimestamp
    private LocalDateTime updated_date;

}
