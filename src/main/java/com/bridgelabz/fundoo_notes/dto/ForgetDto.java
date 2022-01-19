package com.bridgelabz.fundoo_notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Setter
@Getter
@ToString
public class ForgetDto {

    private String name;
    private String email;
    private String number;
    private String password;
}
