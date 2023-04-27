package com.nis.model.dto;

import com.nis.model.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDTO {

    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String signature;
    private PaymentStatus status;
    private String gateway;
}
