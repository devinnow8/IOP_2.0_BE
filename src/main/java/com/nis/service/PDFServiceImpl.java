package com.nis.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;


@Component
public class PDFServiceImpl implements PDFService {

    @Autowired
    private TemplateService templateService;


    @Override
    public byte[] createPdf(String html) {

        ByteArrayOutputStream target = new ByteArrayOutputStream();

//        ConverterProperties converterProperties = new ConverterProperties();
//        converterProperties.setBaseUri("http://localhost:8080");
//
//        HtmlConverter.convertToPdf(html, target, converterProperties);

        byte[] bytes = target.toByteArray();

        return bytes;

    }
}
