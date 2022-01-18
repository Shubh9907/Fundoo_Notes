package com.bridgelabz.fundoo_notes.controller;

import com.bridgelabz.fundoo_notes.dto.ForgetDto;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import com.bridgelabz.fundoo_notes.dto.UserDto;
import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class controller {

    @Autowired
    IService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        System.out.println("User --> " + userDto);
        String msg = service.registerUser(userDto);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        String msg = service.updateUser(id, userDto);
        return new  ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        String msg = service.userLogin(loginDto);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        String msg = service.deleteUser(id);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

    @PutMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetDto forgetDto) {
        String msg = service.forgetPassword(forgetDto);
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

}
