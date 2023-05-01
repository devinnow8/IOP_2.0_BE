package com.nis.service;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
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
    public String createPayementOrderId(float amount, String currency,String invoiceId) throws Exception{

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName("Jenny Rosen")
                        .setAddress(
                                CustomerCreateParams.Address.builder()
                                        .setLine1("510 Townsend St")
                                        .setPostalCode("98140")
                                        .setCity("San Francisco")
                                        .setState("CA")
                                        .setCountry("US")
                                        .build()
                        )
                        .build();

        Customer customer = Customer.create(params);

        PaymentIntentCreateParams createParams = new
                PaymentIntentCreateParams.Builder()
                .setCurrency("usd")
                .setShipping(
                PaymentIntentCreateParams.Shipping.builder()
                        .setName("Jenny Rosen")
                        .setAddress(
                                PaymentIntentCreateParams.Shipping.Address.builder()
                                        .setLine1("510 Townsend St")
                                        .setPostalCode("98140")
                                        .setCity("San Francisco")
                                        .setState("CA")
                                        .setCountry("US")
                                        .build()
                        )
                        .build()
        )
                .setDescription("Payment for invoice:"+invoiceId)
                .putMetadata("featureRequest", "Payment for invoice:"+invoiceId)
                .setAmount((long)(amount * 100))
                .build();

        PaymentIntent intent = PaymentIntent.create(createParams);
        return intent.getClientSecret();
    }
}
