package com.bridgelabz.fundoo_notes.service;

import com.bridgelabz.fundoo_notes.dto.ForgetDto;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import com.bridgelabz.fundoo_notes.dto.UserDto;
import com.bridgelabz.fundoo_notes.entity.User;

public interface IService {
    public String registerUser(UserDto userDto);
    public String userLogin(LoginDto loginDto);
    public String updateUser(int id, UserDto userDto);
    public String deleteUser(int id);
    public String forgetPassword(ForgetDto forgetDto);
}
