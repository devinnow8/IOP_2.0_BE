package com.nis.repository;

import com.nis.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

        Optional<Payment> findByRazorpayOrderId(String orderId);

        List<Payment> findByInvoiceId(String invoiceId);

        @Query(nativeQuery = true, value = "select  *  \n" +
                        "            payment p \n" +
                        "             where p.created_date < NOW() - INTERVAL '1 hours'  and p.status =? ")
        List<Payment> getPendingOrders(String status);

        @Query(nativeQuery = true, value = "select  * \n" +
                        "            payment p \n" +
                        "             where p.created_date < NOW() - INTERVAL '10 minutes'  and p.status =? ")
        List<Payment> getPendingOrders10minutes(String status);
}
