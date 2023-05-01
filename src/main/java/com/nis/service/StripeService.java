package com.nis.service;

public interface StripeService {

    String createPayementOrderId(float amount, String currency,String invoiceID)  throws Exception;
}
