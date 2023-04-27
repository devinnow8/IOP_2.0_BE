package com.nis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Component
public class TemplateService {

    private static Logger logger= LoggerFactory.getLogger(TemplateService.class);
    public String passwordTemplate;
    public String pdfTemplate;


    @Autowired
    public TemplateService(){
    }

    private String loadTemplate(String mailTemplate) throws Exception{
        ClassPathResource resource = new ClassPathResource(File.separator+mailTemplate);
        BufferedReader reader= null;
        reader= new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String content= new String();
        try{
            for(String line;(line=reader.readLine())!=null; content+=line);
        }catch (IOException ex){
            logger.error(ex.getMessage(),ex);
            throw new Exception("Could not read template ="+mailTemplate);
        }

        return content;
    }

    public String getTemplate(String template, Map<String,String> replacement){
        String temp=template;
        for(Map.Entry<String,String> entry: replacement.entrySet()){
            temp=temp.replace("{{"+entry.getKey()+"}}",emptyIfNull(entry.getValue()));
        }
        return  temp;
    }

    private String emptyIfNull(String value){
        if (value==null){
            return "NA";
        }
        return value;
    }
}
