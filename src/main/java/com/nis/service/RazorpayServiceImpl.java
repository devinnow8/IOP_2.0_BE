package com.nis.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.SignatureException;
import java.util.List;

@Service
public class RazorpayServiceImpl implements RazorpayService{

    @Value("${razorpay.api.key}")
    private String keyId;

    @Value("${razorpay.api.secret}")
    private String secret;

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    @Override
    public Order createPaymentOrderId(float amount,String currency) throws Exception{
        RazorpayClient razorpay = new RazorpayClient(keyId, secret);
        JSONObject orderRequest = new JSONObject();
//        Long a= (amount * 100);
        orderRequest.put("amount", amount*100); // amount in the smallest currency unit
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", "order_rcptid_11");

        Order order = razorpay.orders.create(orderRequest);
        return order;
    }


    @Override
    public List<Payment> fetchPaymentDetails(String orderId) throws Exception{
        RazorpayClient razorpay = new RazorpayClient(keyId, secret);

       List<Payment> payments= razorpay.orders.fetchPayments(orderId);
        return payments;
    }

    @Override
    public Order fetchOrderDetails(String orderId) throws Exception{
        RazorpayClient razorpay = new RazorpayClient(keyId, secret);

        Order order= razorpay.orders.fetch(orderId);
        return order;
    }

    public String createSignature(String data) throws java.security.SignatureException
    {
        String result;
        try {

            SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(data.getBytes());

            result = DatatypeConverter.printHexBinary(rawHmac).toLowerCase();

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
}
