package com.nis.service;


import com.nis.entity.*;
import com.nis.exception.RecordExistException;
import com.nis.exception.ResourceNotFoundException;
import com.nis.model.*;
import com.nis.model.dto.InvoiceDTO;
import com.nis.model.dto.PaymentDTO;
import com.nis.repository.InvoiceRepository;
import com.nis.repository.PaymentRepository;
import com.razorpay.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.criteria.Predicate;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SignatureException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private RazorpayService razorpayService;
    @Autowired
    private StripeService stripeService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PDFService pdfService;


    public Specification<Invoice> getInvoiceListQuery(InvoiceListRequest request) throws ResourceNotFoundException {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getSearch() != null && !request.getSearch().isEmpty()) {

                Predicate applicantName
                        = criteriaBuilder.like(criteriaBuilder.lower(root.get("applicant_first_name")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate invoiceId
                        = criteriaBuilder.like(root.get("invoiceId"), "%" + request.getSearch() + "%");
                Predicate merchant
                        = criteriaBuilder.like(criteriaBuilder.lower(root.get("merchant")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate nationality
                        = criteriaBuilder.like(criteriaBuilder.lower(root.get("applicant_nationality")), "%" + request.getSearch().toLowerCase() + "%");
                Predicate amount
                        = criteriaBuilder.like(root.get("total").as(String.class), "%" + request.getSearch() + "%");
                Predicate status
                        = criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), "%" + request.getSearch().toLowerCase() + "%");

                predicates.add(criteriaBuilder.and(criteriaBuilder.or(applicantName, invoiceId, merchant,nationality,amount,status)));
            }

            query.orderBy(criteriaBuilder.desc(root.get("invoiceId")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


    @Override
    public Page<Invoice> getInvoiceList(InvoiceListRequest request) throws ResourceNotFoundException {

        List<Invoice> list = null;
        Page<Invoice> pages = null;
        if (request.getPage_number() == 0) {
            pages = new PageImpl<>(invoiceRepository.findAll(getInvoiceListQuery(request)));
        } else {
            Pageable paging = PageRequest.of(request.getPage_number() - 1, request.getPage_size());
            pages = invoiceRepository.findAll(getInvoiceListQuery(request), paging);
        }
        return pages;
    }


    @Override
    public void updateInvoice(String invoiceId, InvoiceDTO invoiceDTO) throws ResourceNotFoundException {
        Invoice invoice = invoiceRepository.findById(invoiceId
        ).orElseThrow(() -> new ResourceNotFoundException("Invoice not found with:" + invoiceId));

        invoice.setMerchant(invoiceDTO.getMerchant());
        invoice.setMerchant_service(invoiceDTO.getMerchant_service());
        invoice.setApplicant_first_name(invoiceDTO.getApplicant_first_name());
        invoice.setApplicant_last_name(invoiceDTO.getApplicant_last_name());
        invoice.setApplicant_phone_number(invoiceDTO.getApplicant_phone_number());
        invoice.setApplicant_email(invoiceDTO.getApplicant_email());
        invoice.setApplicant_nationality(invoiceDTO.getApplicant_nationality());
        invoice.setProcessing_country(invoiceDTO.getProcessing_country());
        invoice.setProcessing_center(invoiceDTO.getProcessing_center());
        invoice.setService_fee(invoiceDTO.getService_fee());
        invoice.setGateway_fee(invoiceDTO.getGateway_fee());
        invoice.setTotal(invoiceDTO.getTotal());
        invoice.setCurrency(invoiceDTO.getCurrency());
        invoice.setMode_of_payment(invoiceDTO.getMode_of_payment());
        invoice.setUpdated_date(LocalDateTime.now());
        invoiceRepository.save(invoice);

    }

    @Override
    public String saveInvoice(InvoiceDTO invoiceDTO) throws Exception {

        if (invoiceDTO.getInvoice_id()!=null && !invoiceDTO.getInvoice_id().isEmpty()){
            updateInvoice(invoiceDTO.getInvoice_id(),invoiceDTO);
            return invoiceDTO.getInvoice_id();
        }

        Invoice invoice = new Invoice();

        invoice.setMerchant(invoiceDTO.getMerchant());
        invoice.setMerchant_service(invoiceDTO.getMerchant_service());
        invoice.setApplicant_first_name(invoiceDTO.getApplicant_first_name());
        invoice.setApplicant_last_name(invoiceDTO.getApplicant_last_name());
        invoice.setApplicant_phone_number(invoiceDTO.getApplicant_phone_number());
        invoice.setApplicant_email(invoiceDTO.getApplicant_email());
        invoice.setApplicant_nationality(invoiceDTO.getApplicant_nationality());
        invoice.setProcessing_country(invoiceDTO.getProcessing_country());
        invoice.setProcessing_center(invoiceDTO.getProcessing_center());
        invoice.setService_fee(invoiceDTO.getService_fee());
        invoice.setGateway_fee(invoiceDTO.getGateway_fee());
        invoice.setTotal(invoiceDTO.getTotal());
        invoice.setCurrency(invoiceDTO.getCurrency());
        invoice.setMode_of_payment(invoiceDTO.getMode_of_payment());

        invoiceRepository.save(invoice);
        return invoice.getInvoiceId();
    }

    @Override
    public InvoiceResponse saveInvoiceAndCreateOrder(InvoiceDTO invoiceDTO) throws Exception {

        String invoiceId = saveInvoice(invoiceDTO);
        OrderResponse orderResponse = createOrder(invoiceId);

        InvoiceResponse response = new InvoiceResponse();
        response.setInvoice_id(invoiceId);
        response.setOrder_id(orderResponse.getOrder_id());
        response.setGateway(orderResponse.getGateway());
        return response;
    }

    @Override
    public OrderResponse createOrder(String invoiceId) throws Exception {

        Invoice invoice = getInvoiceById(invoiceId);
        String orderId="";
        Payment paymentDetails = new Payment();
        if (invoice.getProcessing_country().equalsIgnoreCase("India")) {
            Order order = razorpayService.createPaymentOrderId(invoice.getTotal(), invoice.getCurrency());
            orderId = order.get("id");
            paymentDetails.setGateway("razorpay");
        }else{
            orderId= stripeService.createPayementOrderId(invoice.getTotal(),invoice.getCurrency());
            paymentDetails.setGateway("stripe");
        }

        // save payment details to db

        paymentDetails.setAmount(invoice.getTotal());
        paymentDetails.setRazorpayOrderId(orderId);
        paymentDetails.setCurrency(invoice.getCurrency());
        paymentDetails.setInvoice_id(invoiceId);

        paymentRepository.save(paymentDetails);

        OrderResponse response= new OrderResponse();
        response.setOrder_id(orderId);
        response.setGateway(paymentDetails.getGateway());
        return response;
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoiceById(String id) throws ResourceNotFoundException {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found:" + id));

        return invoice;
    }


    @Override
    public byte[] getInvoicePdf(String appointmentId) throws Exception {
        Invoice appointment = invoiceRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found:" + appointmentId));

        Map<String, String> values = getInvoiceDetailsInMap(appointment);

        String html = templateService.getTemplate(templateService.pdfTemplate, values);
        return pdfService.createPdf(html);
    }

    @Override
    public Map<String, String> getInvoiceDetailsInMap(Invoice invoice) {
//
//        Optional<Invoice> opApplication = applicationRepository.findByApplicationId(appointment.getApplicationId());
//
        Map<String, String> values = new HashMap<>();
//
//        Application application = new Application();
//        if (opApplication.isPresent()) {
//            application = opApplication.get();
//        }
//
//        // creating a barcode image
//        try {
////            byte[] pngImageData = generateBarcodeImage(appointment.getAppointmentId().toString());
//            String url = userURL + "/reschedule-appointment/?appointmentId=" + appointment.getAppointmentId().toString();
//            byte[] pngImageData = generateQRCode(url);
//            values.put("Bar_Code", Base64.getEncoder().encodeToString(pngImageData));
//        } catch (IOException | WriterException ex) {
//            logger.error("Error creating barcode ", ex.getMessage());
//            values.put("Bar_Code", "");
//        }
//
//        values.put("Passport_Number", emptyIfNull(application.getPassport()));
//        values.put("POB", emptyIfNull(application.getPlace_of_birth()));
//        values.put("Nationality", emptyIfNull(application.getNationality()));
//        values.put("Appointment_Id", emptyIfNull(appointment.getAppointmentId()));
//        values.put("Application_Id", appointment.getApplicationId());
//        values.put("Created_Date", emptyIfNull(appointment.getCreated_date()));
//        values.put("Appointment_Date", emptyIfNull(appointment.getAppointmentDate()) + " " + emptyIfNull(appointment.getAppointmentTime()));
//        values.put("Service", appointment.getServiceType());
//        values.put("Name", appointment.getApplicantFullName());
//        values.put("Applicant_Name", appointment.getApplicantFullName());
//        values.put("DOB", emptyIfNull(appointment.getDob()));
//        values.put("Email", emptyIfNull(appointment.getEmail()));
//        values.put("Phone_Numer", emptyIfNull(appointment.getPhone_number()));
//        values.put("Category", appointment.getCategory());
//        values.put("User_Url", userURL);
//        values.put("Amount",appointment.getPrice()+" "+appointment.getCurrency());
//
//        if (appointment.getCenter() != null) {
//            values.put("Center_Address", emptyIfNull(appointment.getCenter().getCenterName() + ", "
//                    + appointment.getCenter().getAddress() + " "
//                    + appointment.getCenter().getCity() + ", "
//                    + appointment.getCenter().getState() + ", "
//                    + appointment.getCenter().getCountry()));
//            values.put("Center_Phone", emptyIfNull(appointment.getCenter().getPhone_number()));
//            values.put("Center_Timezone", "(" + emptyIfNull(appointment.getCenter().getTimeZone()) + ")");
//        } else {
//            values.put("Center_Address", "NA");
//            values.put("Center_Phone", "NA");
//            values.put("Center_Timezone", "");
//        }
//
//
        return values;
    }


    @Override
    public void confirmOrder(PaymentDTO paymentDTO) throws Exception {

        // find payment object
        Payment payment = paymentRepository.findByRazorpayOrderId(paymentDTO.getRazorpay_order_id())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with order id:" + paymentDTO.getRazorpay_order_id()));

        if (payment.getStatus() == PaymentStatus.Complete) {
            return;
        }

        if(payment.getStatus()!=null && paymentDTO.getStatus()==PaymentStatus.Failed){
            payment.setStatus(PaymentStatus.Failed);
            paymentRepository.save(payment);
            updateInvoicePayment(payment.getInvoice_id(), payment.getId(),PaymentStatus.Failed);
        }else if (paymentDTO.getGateway().equalsIgnoreCase("razorpay")){
            String signature = razorpayService.createSignature(
                    payment.getRazorpayOrderId() + "|" + paymentDTO.getRazorpay_payment_id());

            // verify signature
            if (!signature.equalsIgnoreCase(paymentDTO.getSignature())) {
                throw new SignatureException("Signature did not match ");
            }

            //update payment status
            payment.setStatus(PaymentStatus.Complete);
            payment.setRazorpayPaymentId(paymentDTO.getRazorpay_payment_id());
            payment.setSignature(paymentDTO.getSignature());

            paymentRepository.save(payment);

            updateInvoicePayment(payment.getInvoice_id(), payment.getId(),PaymentStatus.Complete);
        } else if (paymentDTO.getGateway().equalsIgnoreCase("stripe")) {
            payment.setStatus(PaymentStatus.Complete);
            paymentRepository.save(payment);
            updateInvoicePayment(payment.getInvoice_id(), payment.getId(),PaymentStatus.Complete);
        }


    }


    @Override
    public void updateInvoicePayment(String invoiceId, Long paymentId,PaymentStatus status) throws ResourceNotFoundException {

        Invoice invoice = getInvoiceById(invoiceId);

        invoice.setPayment_id(paymentId);
        invoice.setStatus(status);

        invoiceRepository.save(invoice);
    }


    @Override
    public String confirmPayment(String orderId, Map<String, Object> transDetails) throws Exception {

        if (transDetails.containsKey("razorpay_payment_id")) {

            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setRazorpay_order_id(transDetails.get("razorpay_order_id").toString());
            paymentDTO.setRazorpay_payment_id(transDetails.get("razorpay_payment_id").toString());
            paymentDTO.setSignature(transDetails.get("razorpay_signature").toString());
            confirmOrder(paymentDTO);

            return "Payment received successfully";
        } else {

            System.out.println(transDetails.get("error"));
            Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with order id:" + orderId));

            payment.setStatus(PaymentStatus.Failed);
//            payment.setErrorDescription(transDetails.get("description"));
            paymentRepository.save(payment);

            return "Payment failed.Please try again.";
        }


    }

    private String emptyIfNull(Object val) {
        if (val == null) {
            return "NA";
        } else if (val.toString().isEmpty()) {
            return "NA";
        }
        return val.toString();
    }

}
