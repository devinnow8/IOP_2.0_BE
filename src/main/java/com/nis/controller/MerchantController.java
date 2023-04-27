package com.nis.controller;

import com.nis.entity.Country;
import com.nis.entity.Merchant;
import com.nis.service.MerchantService;
import com.nis.service.ProcessingCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nis")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @GetMapping(value = {"/merchant-list"})
    public ResponseEntity<?> getMerchantList() {

        List<Merchant> merchants = merchantService.getMerchants();
        return new ResponseEntity<>(merchants, HttpStatus.OK);

    }
}
