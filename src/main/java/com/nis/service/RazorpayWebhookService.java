package com.nis.service;

import com.nis.model.razorpay.Event;

public interface RazorpayWebhookService {
    void updateOrder(Event event) throws Exception;
}
