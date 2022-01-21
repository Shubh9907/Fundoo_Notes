package com.bridgelabz.fundoo_notes.controller;

import com.bridgelabz.fundoo_notes.dto.PasswordDto;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import com.bridgelabz.fundoo_notes.dto.UserDto;
import com.bridgelabz.fundoo_notes.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author Shubham Verma
 */
@RestController
public class controller {

    @Autowired
    IService service;

//    this api is used to register new user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        System.out.println("User --> " + userDto);
        String msg = service.registerUser(userDto);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    //    this api is used to update the user
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        String msg = service.updateUser(id, userDto);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    //    this api is used to log in
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        String msg = service.userLogin(loginDto);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    //    this api is used to delete the user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        String msg = service.deleteUser(id);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    //    this api is used to send the password reset link to the user
    @GetMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestParam String email) {
        String msg = service.forgetPassword(email);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    //    this api is used to change the password
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam String token, @RequestBody PasswordDto passwordDto) {
        String msg = service.changePassword(token , passwordDto);
        return new ResponseEntity<String>(msg,HttpStatus.OK);
    }

    //    this api is used to verify the user
    @GetMapping("/verifyUser/{token}")
    public ResponseEntity<String> verifyUser(@PathVariable String token) {
        String msg = service.verifyUser(token);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

}
