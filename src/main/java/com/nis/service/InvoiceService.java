package com.nis.service;

import com.nis.entity.Invoice;
import com.nis.exception.ResourceNotFoundException;
import com.nis.model.*;
import com.nis.model.dto.InvoiceDTO;
import com.nis.model.dto.PaymentDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InvoiceService {

    Page<Invoice> getInvoiceList(InvoiceListRequest request) throws ResourceNotFoundException;
    void updateInvoice(String invoiceId,InvoiceDTO invoiceDTO) throws ResourceNotFoundException;

    String saveInvoice(InvoiceDTO invoiceDTO) throws Exception;

    InvoiceResponse saveInvoiceAndCreateOrder(InvoiceDTO invoiceDTO) throws Exception;

    byte[] getInvoicePdf(String invoiceId) throws Exception;

    Invoice getInvoiceById(String id) throws ResourceNotFoundException ;

    Map<String, String> getInvoiceDetailsInMap(Invoice invoice);

    void updateInvoicePayment(String invoiceId, Long paymentId,PaymentStatus status) throws ResourceNotFoundException;
    OrderResponse createOrder(String invoiceId) throws Exception;
    void confirmOrder(PaymentDTO paymentDTO) throws Exception;
    String confirmPayment(String orderId, Map<String, Object> transDetails) throws Exception;
    void saveInvoice(Invoice invoice);

}
