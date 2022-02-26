package com.bridgelabz.fundoo_notes.user.service;

import com.bridgelabz.fundoo_notes.user.dto.LoginDto;
import com.bridgelabz.fundoo_notes.user.dto.PasswordDto;
import com.bridgelabz.fundoo_notes.user.dto.UserDto;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

public interface IService {
	public ApiResponse getAllUsers();
	
    public ApiResponse registerUser(UserDto userDto);

    public ApiResponse userLogin(LoginDto loginDto);

    public ApiResponse updateUser(int id, UserDto userDto);

    public ApiResponse deleteUser(int id);

    public ApiResponse forgetPassword(String email);

    public ApiResponse changePassword(String token, PasswordDto passwordDto);

    public String verifyUser(String token);
}