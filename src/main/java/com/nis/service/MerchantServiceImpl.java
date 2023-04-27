package com.nis.service;

import com.nis.entity.Merchant;
import com.nis.model.UserAppointmentSlot;
import com.nis.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {


    @Autowired
    private MerchantRepository merchantRepository;


    @Override
    public List<Merchant> getMerchants() {

        return merchantRepository.findAll();
    }
}
