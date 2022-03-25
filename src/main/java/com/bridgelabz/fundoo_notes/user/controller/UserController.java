package com.bridgelabz.fundoo_notes.user.controller;

import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.user.dto.LoginDto;
import com.bridgelabz.fundoo_notes.user.dto.PasswordDto;
import com.bridgelabz.fundoo_notes.user.dto.UserDto;
import com.bridgelabz.fundoo_notes.user.repository.UserRepository;
import com.bridgelabz.fundoo_notes.user.service.IService;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;


/**
 * @author Shubham Verma
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class UserController {

    @Autowired
    IService service;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users")
    @ApiOperation("This api is used for getting all records from User")
    public ResponseEntity<ApiResponse> getAllUser() {
    	ApiResponse response = service.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    this api is used to register new user
    @PostMapping("/user")
    @ApiOperation("This api is used for user registeration")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid UserDto userDto) {
        ApiResponse response = service.registerUser(userDto);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
    }

    //    this api is used to update the user
    @PutMapping("/update/{id}")
    @ApiOperation("This api is used for update a user")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        ApiResponse response = service.updateUser(id, userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    this api is used to log in
    @PostMapping("/login")
    @ApiOperation("This api is used for user login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginDto loginDto) {
        ApiResponse response = service.userLogin(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    this api is used to delete the user
    @DeleteMapping("/delete/{id}")
    @ApiOperation("This api is used for delete a user")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer id) {
    	ApiResponse response = service.deleteUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    this api is used to send the password reset link to the user
    @GetMapping("/forgetPassword")
    @ApiOperation("This api is used for sending the password reset link")
    public ResponseEntity<ApiResponse> forgetPassword(@RequestParam String email) {
        ApiResponse response = service.forgetPassword(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    this api is used to change the password
    @PutMapping("/changePassword")
    @ApiOperation("This api is used for reseting the password")
    public ResponseEntity<ApiResponse> changePassword(@RequestParam String token, @RequestBody PasswordDto passwordDto) {
        ApiResponse response = service.changePassword(token, passwordDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //    this api is used to verify the user
    @GetMapping("/verifyUser/{token}")
    @ApiOperation("This api is used for verifing the user")
    public ResponseEntity<String> verifyUser(@PathVariable String token) {
        String msg = service.verifyUser(token);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

}
