package com.nis.service;

import com.nis.entity.MailDetail;
import com.nis.model.MailStatus;
import com.nis.repository.MailServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MailServiceImpl implements MailService{

    @Autowired
    private MailServiceRepository mailServiceRepository;
    @Override
    public List<MailDetail> getMailsByStatus(MailStatus status) {
        return mailServiceRepository.findMailDetailByMailStatus(status);
    }

    @Override
    public void updateMailStatus(Long id, MailStatus status,String errorMsg) {
        Optional<MailDetail> mailDetail= mailServiceRepository.findById(id);
        if(mailDetail.isPresent()){
            MailDetail entry=mailDetail.get();
            entry.setMailStatus(status);
            entry.setMailError(errorMsg);
            mailServiceRepository.save(entry);
        }

    }

    @Override
    public void createMailEntry(MailDetail mailDetail) {
        mailServiceRepository.save(mailDetail);
    }
}
