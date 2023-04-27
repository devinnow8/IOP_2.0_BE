package com.nis.service;

import com.nis.entity.MailDetail;
import com.nis.model.MailStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MailService {

    List<MailDetail> getMailsByStatus(MailStatus status);

    void updateMailStatus(Long id,MailStatus status,String erroMsg);

    void createMailEntry(MailDetail mailDetail);



}
