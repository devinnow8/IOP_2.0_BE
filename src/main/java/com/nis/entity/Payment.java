package com.nis.entity;

import com.nis.model.PaymentStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoice_id;
    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String signature;

    private String currency;

    private float amount;

    private String gateway;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status=PaymentStatus.Pending;

    private String errorDescription;

    @CreationTimestamp
    private LocalDate created_date;
}
