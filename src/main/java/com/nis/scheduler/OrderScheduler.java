package com.nis.scheduler;

import com.nis.entity.Payment;
import com.nis.model.PaymentStatus;
import com.nis.repository.PaymentRepository;
import com.nis.service.InvoiceService;
import com.nis.service.RazorpayService;
import com.razorpay.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderScheduler.class);

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RazorpayService razorpayService;

    @Autowired
    private InvoiceService invoiceService;
    @Scheduled(fixedRateString = "${mail.scheduler.interval}")
    private void updatePayments(){

        updatePaymentStatus();
        removePendingPayments();
    }


    private void updatePaymentStatus() {
        List<Payment> orderList= paymentRepository.getPendingOrders10minutes(PaymentStatus.Pending.toString());

        for(Payment order:orderList){
            try {
               Order razorpayOrder= razorpayService.fetchOrderDetails(order.getRazorpayOrderId());
               String status=razorpayOrder.get("status");
                System.out.println(status);
                if(status=="paid"){
                    order.setStatus(PaymentStatus.Complete);
                    invoiceService.updateInvoicePayment(order.getInvoiceId(),order.getId(),PaymentStatus.Complete);
                }else if(status==""){
                    order.setStatus(PaymentStatus.Cancelled);
                }
                paymentRepository.save(order);
            }catch (Exception ex){
                logger.error("Error in fetching order details",ex);
            }
        }
    }
    private void removePendingPayments() {
        List<Payment> orderList= paymentRepository.getPendingOrders(PaymentStatus.Pending.toString());

        for(Payment order:orderList){
            order.setStatus(PaymentStatus.Cancelled);
        }

        paymentRepository.saveAll(orderList);
    }
}
