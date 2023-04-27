package com.nis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
public class NISApplication {

    public static void main(String[] args) {
        SpringApplication.run(NISApplication.class,args);
    }

}
