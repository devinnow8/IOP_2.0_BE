package com.nis.service;

import com.razorpay.Order;
import com.razorpay.Payment;

import java.util.List;

public interface RazorpayService {
    Order createPaymentOrderId(float amount, String currency) throws Exception;

    String createSignature(String data) throws Exception;
    List<Payment> fetchPaymentDetails(String orderId) throws Exception;
    Order fetchOrderDetails(String orderId) throws Exception;
}
