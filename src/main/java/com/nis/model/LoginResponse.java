package com.nis.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private Long user_id;
    private String name;
    private String jwt;
    private String email;
}
