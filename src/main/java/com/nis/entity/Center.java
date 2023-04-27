package com.nis.entity;

import com.nis.model.CenterPaymentMode;
import com.nis.model.Status;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id")
    private Long centerId;
    @Column(name = "center_name")
    private String centerName;

}
