package com.nis.controller;

import com.nis.entity.Center;
import com.nis.entity.Country;
import com.nis.service.ProcessingCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nis")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CenterController {

    @Autowired
    private ProcessingCenterService centerDetailService;
    @GetMapping(value = {"/center-list"})
    public ResponseEntity<?> getCenterList() {

        List<Country> centerList = centerDetailService.getCenterList();
        return new ResponseEntity<>(centerList, HttpStatus.OK);

    }
}
