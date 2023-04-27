package com.nis.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StripeServiceImpl implements StripeService{

    @Value("${stripe.api.key}")
    private String stripeKey;
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }

    @Override
    public String createPayementOrderId(float amount, String currency) throws Exception{
        PaymentIntentCreateParams createParams = new
                PaymentIntentCreateParams.Builder()
                .setCurrency("usd")
//                .putMetadata("featureRequest", createPayment.getFeatureRequest())
                .setAmount((long)(amount * 100))
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);
        return intent.getClientSecret();
    }
}
