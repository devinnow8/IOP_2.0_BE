package com.nis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class PasswordServiceImpl implements PasswordService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private String specialCharacters = "!@#$";
    private String numbers = "1234567890";

    private int passwordLength=8;

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
    public String generateRandomPassword() {

        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[passwordLength];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< passwordLength ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password.toString();
    }

}
