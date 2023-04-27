package com.nis.repository;

import com.nis.entity.Invoice;
import com.nis.model.AppointmentStatus;
import com.nis.model.UserAppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,String>, JpaSpecificationExecutor<Invoice> {

}
