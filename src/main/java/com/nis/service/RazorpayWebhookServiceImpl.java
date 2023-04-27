package com.nis.service;

import com.nis.entity.Payment;
import com.nis.exception.ResourceNotFoundException;
import com.nis.model.PaymentStatus;
import com.nis.model.razorpay.Entity;
import com.nis.model.razorpay.Event;
import com.nis.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RazorpayWebhookServiceImpl implements RazorpayWebhookService{

    private static Logger logger= LoggerFactory.getLogger(RazorpayWebhookServiceImpl.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Override
    public void updateOrder(Event event) throws Exception{
        logger.info(event.toString());
        Entity entity = event.getPayload().getPayment().getEntity();

        if (event.entity.equals("event") && entity.status.equalsIgnoreCase("captured")) {
            logger.info("Payment received for order: "+entity.order_id);
            Payment payment = paymentRepository.findByRazorpayOrderId(entity.order_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with order id:" + entity.order_id));

            if (payment.getStatus()!= PaymentStatus.Complete) {

                logger.info("Updated payment status to complete");
                //update payment status
                payment.setStatus(PaymentStatus.Complete);
                payment.setRazorpayPaymentId(entity.id);
                paymentRepository.save(payment);

                invoiceService.updateInvoicePayment(payment.getInvoice_id(),payment.getId(),PaymentStatus.Complete);

            }else{
                logger.info("Payment status already upated");
            }
        }else if (event.entity.equals("event") && entity.status.equalsIgnoreCase("failed")) {
            logger.info("Payment failed for order: "+entity.order_id);
            Payment payment = paymentRepository.findByRazorpayOrderId(entity.order_id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with order id:" + entity.order_id));
            //update payment status
            payment.setStatus(PaymentStatus.Failed);
            payment.setRazorpayPaymentId(entity.id);
            paymentRepository.save(payment);
        }
    }
}
