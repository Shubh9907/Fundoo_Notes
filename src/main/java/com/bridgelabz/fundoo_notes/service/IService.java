package com.bridgelabz.fundoo_notes.service;

import com.bridgelabz.fundoo_notes.dto.PasswordDto;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import com.bridgelabz.fundoo_notes.dto.UserDto;

public interface IService {
    public String registerUser(UserDto userDto);
    public String userLogin(LoginDto loginDto);
    public String updateUser(int id, UserDto userDto);
    public String deleteUser(int id);
    public String forgetPassword(String email);
    public String changePassword(String token, PasswordDto passwordDto);
    public String verifyUser(String token);
}
