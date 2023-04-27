package com.nis.service;

public interface PasswordService {
    public String generateRandomPassword();

    String encodePassword(String password);
}
