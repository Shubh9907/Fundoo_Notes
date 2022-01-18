package com.bridgelabz.fundoo_notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@ToString
public class UserDto {

    private String name;
    private String email;
    private String password;
    private String number;

    public void setPassword(String password) {
        String encPass = null;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        encPass = bCryptPasswordEncoder.encode(password);
        this.password = encPass;
    }
}
