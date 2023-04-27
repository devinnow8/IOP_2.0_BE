package com.nis.repository;

import com.nis.entity.MailDetail;
import com.nis.model.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailServiceRepository extends JpaRepository<MailDetail,Long> {

    List<MailDetail> findMailDetailByMailStatus(MailStatus status);
}
