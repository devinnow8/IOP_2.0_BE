package com.nis.controller;

import com.nis.entity.Invoice;
import com.nis.exception.ResourceNotFoundException;
import com.nis.model.InvoiceListResponse;
import com.nis.model.InvoiceListRequest;
import com.nis.model.InvoiceResponse;
import com.nis.model.dto.InvoiceDTO;
import com.nis.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nis/invoice")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/pdf/{invoiceId}")
    public ResponseEntity<?> getInvoicePdf(@PathVariable String invoiceId) throws Exception {

        byte[] pdfBytes = invoiceService.getInvoicePdf(invoiceId);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + invoiceId + ".pdf");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }

    @PostMapping("/save")
    public ResponseEntity<?> saveInvoice(@RequestBody InvoiceDTO invoiceDTO) throws Exception {
        String appointmentId = invoiceService.saveInvoice(invoiceDTO);
        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice_id(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/save/create-order")
    public ResponseEntity<?> saveInvoiceAndCreateOrder(@RequestBody InvoiceDTO invoiceDTO) throws Exception {
        InvoiceResponse response = invoiceService.saveInvoiceAndCreateOrder(invoiceDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<?> getInvoice(@PathVariable String invoiceId) throws ResourceNotFoundException {

        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        return new ResponseEntity<>(invoice,HttpStatus.OK);

    }


    @PutMapping("/{invoiceId}")
    public ResponseEntity<?> updateInvoice(@PathVariable String invoiceId, @RequestBody InvoiceDTO invoiceDTO) throws Exception {
        invoiceService.updateInvoice(invoiceId, invoiceDTO);
        return new ResponseEntity<>("Record Updated!", HttpStatus.OK);

    }

    @PostMapping(value = {"/list"})
    public ResponseEntity<?> getInvoiceList(@RequestBody InvoiceListRequest request) throws Exception {

        InvoiceListResponse response= new InvoiceListResponse();
        Page<Invoice> pages = invoiceService.getInvoiceList( request);
        if (pages != null && pages.getContent() != null) {
            response.setAppointmentList(pages.getContent());
            response.setPage_number(pages.getNumber()+1);
            response.setTotal_count(pages.getTotalElements());
            response.setTotal_pages(pages.getTotalPages());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


}
