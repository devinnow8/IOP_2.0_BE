package com.nis.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MerchantService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    private String service;


}
