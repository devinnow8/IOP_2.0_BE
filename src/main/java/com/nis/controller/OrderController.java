package com.nis.controller;

import com.nis.model.InvoiceResponse;
import com.nis.model.OrderResponse;
import com.nis.model.SuccessResponse;
import com.nis.model.dto.OrderDTO;
import com.nis.model.dto.PaymentDTO;
import com.nis.model.razorpay.Event;
import com.nis.service.InvoiceService;
import com.nis.service.RazorpayWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nis/order")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RazorpayWebhookService razorpayWebhookService;

    @PostMapping("/create")
    public ResponseEntity<?> generateOrder(@RequestBody OrderDTO orderDTO) throws Exception {

        OrderResponse response=invoiceService.createOrder(orderDTO.getInvoice_id());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/confirm-order")
    public ResponseEntity<?> confirmOrder(@RequestBody PaymentDTO paymentDTO) throws Exception {

        invoiceService.confirmOrder(paymentDTO);
        SuccessResponse response= new SuccessResponse();
        response.setMsg("Payment status updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/confirm-payment/{orderId}")
    public ResponseEntity<?> confirmPayment(@PathVariable String orderId,@RequestBody Map<String,Object> transDetail) throws Exception {

      String message=  invoiceService.confirmPayment(orderId,transDetail);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }

    @PostMapping("/webhook")
    public ResponseEntity webhook(@RequestBody Event event) throws Exception{

        razorpayWebhookService.updateOrder(event);
        return ResponseEntity.ok("Record Updated!");
    }


}
