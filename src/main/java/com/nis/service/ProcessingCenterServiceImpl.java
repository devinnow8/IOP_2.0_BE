package com.nis.service;

import com.nis.entity.*;
import com.nis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessingCenterServiceImpl implements ProcessingCenterService {


    @Autowired
    private ProcessingCenterRepository centerDetailRepository;

    @Override
    public List<Country> getCenterList() {

        return centerDetailRepository.findAll();

    }


}
