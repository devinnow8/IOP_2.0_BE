package com.nis.entity;

import com.nis.model.MailStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class MailDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mail_from")
    private String mailFrom;

    @Column(name = "mail_to")
    private String mailTo;

    @Column(name = "mail_subject")
    private String mailSubject;

    @Column(name = "mail_body",columnDefinition = "varchar(4000)")
    private String mailBody;

    @Column(name = "mail_attachment_path")
    private String mailAttachmentPath;

    @Column(name = "mail_status")
    @Enumerated(EnumType.STRING)
    private MailStatus mailStatus=MailStatus.Pending;

    @CreationTimestamp
    @Column(name = "mail_date")
    private LocalDateTime sentDate;

    @Column(name = "mail_error")
    private String mailError;

}
