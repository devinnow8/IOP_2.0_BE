package com.nis.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long user_id;
    private String firstname;
    private String lastname;
    @Email
    private String email;
    private String phone_number;
    private String password;
    private LocalDateTime last_login;

}
