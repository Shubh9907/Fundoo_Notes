package com.bridgelabz.fundoo_notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginDto {

    private String email;
    private String password;
}
