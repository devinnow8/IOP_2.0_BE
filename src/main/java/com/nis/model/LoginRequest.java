package com.nis.model;

import lombok.Data;

@Data
public class LoginRequest {

    private String username_email;
    private String password;
}
