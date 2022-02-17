package com.bridgelabz.fundoo_notes.dto;

import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

	@Pattern(regexp = "$[a-z]{3,}")
    private String name;
    private String email;
    private String password;
    private String number;
}
