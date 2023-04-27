package com.nis.service;

public interface StripeService {

    String createPayementOrderId(float amount, String currency)  throws Exception;
}
