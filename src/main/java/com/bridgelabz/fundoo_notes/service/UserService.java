package com.bridgelabz.fundoo_notes.service;

import com.bridgelabz.fundoo_notes.dto.ForgetDto;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import com.bridgelabz.fundoo_notes.dto.UserDto;
import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.exception.UserException;
import com.bridgelabz.fundoo_notes.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService implements IService{

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtToken jwtToken;

    @Override
    public String registerUser(UserDto userDto) throws UserException {
        User user = modelMapper.map(userDto, User.class);
        User alreadyRegisteredUser = userRepository.findByEmail(user.getEmail());
        user.setRegisterDate(new Date());
        if (alreadyRegisteredUser != null) {
            return  "This Email is already registered with us";
        }
        userRepository.save(user);
        return "User is Successfully stored in db " + user ;
    }

    @Override
    public String userLogin(LoginDto loginDto) {
        User registeredUser = userRepository.findByEmail(loginDto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (registeredUser != null) {
            if (encoder.matches(loginDto.getPassword(),registeredUser.getPassword())) {
                String token = jwtToken.generateToken(loginDto);
                return "Login Successfully with " +loginDto.getEmail()+ " Token= " +token;
            }
        }
        return  "Invalid Email Id or Password";
    }

    @Override
    public String updateUser(int id, UserDto userDto) {
        User registeredUser = userRepository.findById(id);
        if (registeredUser != null) {
            if (userDto.getName() != null) {
                registeredUser.setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                registeredUser.setEmail(userDto.getEmail());
            }
            if (userDto.getPassword() != null) {
                registeredUser.setPassword(userDto.getPassword());
            }
            if (userDto.getNumber() != null) {
                registeredUser.setNumber(userDto.getNumber());
            }
            userRepository.save(registeredUser);
            return "Details successfully updated";
        } else return  "User not found";
    }

    @Override
    public String deleteUser(int id) {
        User registeredUser = userRepository.findById(id);
        if (registeredUser != null) {
            userRepository.deleteById(id);
            return "User Successfully deleted from db";
        }else
        return  "Invalid Details";
    }

    @Override
    public String forgetPassword(ForgetDto forgetDto) {
        User registeredUser = userRepository.findByEmail(forgetDto.getEmail());
        if (registeredUser != null) {
            if ((forgetDto.getName()).matches(registeredUser.getName()) && (forgetDto.getNumber()).matches(registeredUser.getNumber()) && forgetDto.getPassword() != null) {
                registeredUser.setPassword(forgetDto.getPassword());

                userRepository.save(registeredUser);
                return "Password Successfully Changed";
            }
        }
        return  "You Entered wrong details";
    }
}