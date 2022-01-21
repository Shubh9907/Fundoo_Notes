package com.bridgelabz.fundoo_notes.service;

import com.bridgelabz.fundoo_notes.dto.PasswordDto;
import com.bridgelabz.fundoo_notes.dto.LoginDto;
import com.bridgelabz.fundoo_notes.dto.UserDto;
import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.exception.UserException;
import com.bridgelabz.fundoo_notes.repository.UserRepository;
import com.bridgelabz.fundoo_notes.utility.JwtToken;
import com.bridgelabz.fundoo_notes.utility.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Shubham Verma
 */
@Service
public class UserService implements IService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtToken jwtToken;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    MailService mailService;

    @Autowired
    Environment environment;

    @Override
    public String registerUser(UserDto userDto) throws UserException {
        User user = modelMapper.map(userDto, User.class);
        User alreadyRegisteredUser = userRepository.findByEmail(user.getEmail());
        user.setRegisterDate(new Date());
        user.setPassword(encoder.encodePassword(userDto.getPassword()));
        if (alreadyRegisteredUser != null) {
            return environment.getProperty("user.alreadyRegistered");

        }
        userRepository.save(user);
        String msg = mailService.sendMail(userDto.getEmail());
        return environment.getProperty("user.successfullyRegistered") + " " + user;
    }

    @Override
    public String userLogin(LoginDto loginDto) {
        User registeredUser = userRepository.findByEmail(loginDto.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (registeredUser != null) {
            if (!registeredUser.getIsVerified()) {
                return environment.getProperty("user.verifyEmail");
            }
            if (encoder.matches(loginDto.getPassword(), registeredUser.getPassword())) {
                String token = jwtToken.generateToken(loginDto.getEmail());
                return "Login Successfully with " + loginDto.getEmail() + " Token= " + token;
            }
        }
        return environment.getProperty("user.invalidUser");
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
            return environment.getProperty("user.update");
        } else return environment.getProperty("user.invalidDetails");
    }

    @Override
    public String deleteUser(int id) {
        User registeredUser = userRepository.findById(id);
        if (registeredUser != null) {
            userRepository.deleteById(id);
            return environment.getProperty("user.deleted");
        } else
            return environment.getProperty("user.invalidDetails");
    }

    @Override
    public String forgetPassword(String email) {
        User registeredUser = userRepository.findByEmail(email);
        if (registeredUser != null) {
            mailService.throughForget = true;
            return mailService.sendMail(email);
        }
        return environment.getProperty("user.invalidDetails");
    }

    @Override
    public String changePassword(String token, PasswordDto passwordDto) {
        try {
            String email = jwtToken.decodeToken(token);
            String encPass;
            User registeredUser = userRepository.findByEmail(email);
            if (registeredUser != null) {
                if ((passwordDto.getNewPassword()).matches((passwordDto.getConfirmPassword()))) {
                    encPass = encoder.encodePassword(passwordDto.getNewPassword());
                    registeredUser.setPassword(encPass);
                    userRepository.save(registeredUser);
                    return environment.getProperty("user.passwordChanged");
                } else return environment.getProperty("differentPassword");
            }
        }catch (Exception ignored) {}
        return environment.getProperty("user.invalidToken");
    }

    @Override
    public String verifyUser(String token) {
        String email = jwtToken.decodeToken(token);
        User registeredUser = userRepository.findByEmail(email);
        if (registeredUser != null) {
            registeredUser.setIsVerified(true);
            userRepository.save(registeredUser);
            return environment.getProperty("user.verified");
        }
        return environment.getProperty("error");
    }
}