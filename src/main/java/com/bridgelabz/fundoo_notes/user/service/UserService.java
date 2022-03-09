package com.bridgelabz.fundoo_notes.user.service;

import com.bridgelabz.fundoo_notes.configuration.RabbitConfiguration;
import com.bridgelabz.fundoo_notes.entity.User;
import com.bridgelabz.fundoo_notes.user.dto.LoginDto;
import com.bridgelabz.fundoo_notes.user.dto.PasswordDto;
import com.bridgelabz.fundoo_notes.user.dto.UserDto;
import com.bridgelabz.fundoo_notes.user.exception.UserException;
import com.bridgelabz.fundoo_notes.user.repository.UserRepository;
import com.bridgelabz.fundoo_notes.utility.JwtToken;
import com.bridgelabz.fundoo_notes.utility.MailService;
import com.bridgelabz.fundoo_notes.utility.PasswordEncoder;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    
    @Autowired
    ApiResponse apiResponse;
    
    @Autowired
    RabbitTemplate template;
    
    ApiResponse userNotFound = new ApiResponse("User Not Found", 601, null);

    @Override
    public ApiResponse registerUser(UserDto userDto) throws UserException {
        User user = modelMapper.map(userDto, User.class);
        boolean alreadyRegisteredUser = userRepository.findByEmail(user.getEmail()).isPresent();
        user.setRegisterDate(new Date());
        user.setPassword(encoder.encodePassword(userDto.getPassword()));
        if (alreadyRegisteredUser) {
        	apiResponse = new ApiResponse(environment.getProperty("user.alreadyRegistered"), 1, null);
        	
        	return apiResponse;

        }
        userRepository.save(user);
        template.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY1, userDto.getEmail());        
    	apiResponse = new ApiResponse(environment.getProperty("user.successfullyRegistered"), 2, null);
    	
    	return apiResponse;
    }

    @Override
    public ApiResponse userLogin(LoginDto loginDto) {
        User registeredUser = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(()-> new UserException());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (registeredUser != null) {
            if (!registeredUser.getIsVerified()) {
            	apiResponse = new ApiResponse(environment.getProperty("user.verifyEmail"), 1, null);
            	
            	return apiResponse;
            	
            }
            if (encoder.matches(loginDto.getPassword(), registeredUser.getPassword())) {
                String token = jwtToken.generateToken(loginDto.getEmail());
            	apiResponse = new ApiResponse(environment.getProperty("user.loginSuccessfully"), 2, token);
            	
            	return apiResponse;
            }
        }
    	apiResponse = new ApiResponse(environment.getProperty("user.invalidUser"), 3, null);
    	
    	return apiResponse;
    }

    @Override
    public ApiResponse updateUser(int id, UserDto userDto) {
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
            
        	apiResponse = new ApiResponse(environment.getProperty("user.update"), 1, null);
        	
        	return apiResponse;
        	
        } else {
        	apiResponse = new ApiResponse(environment.getProperty("user.invalidDetails"), 2, null);
    	
        	return apiResponse;
        }
    }

    @Override
    public ApiResponse deleteUser(int id) {
        User registeredUser = userRepository.findById(id);
        if (registeredUser != null) {
            userRepository.deleteById(id);
            
        	apiResponse = new ApiResponse(environment.getProperty("user.deleted"), 1, null);
    	
        	return apiResponse;
        } else {
        	apiResponse = new ApiResponse(environment.getProperty("user.invalidDetails"), 2, null);
    	
        	return apiResponse;
        }
    }

    @Override
    public ApiResponse forgetPassword(String email) {
        User registeredUser = userRepository.findByEmail(email).orElseThrow(()-> new UserException());
        if (registeredUser != null) {
            template.convertAndSend(RabbitConfiguration.EXCHANGE, RabbitConfiguration.ROUTING_KEY2, email);        
            
        	apiResponse = new ApiResponse(environment.getProperty("emailSent"), 1, null);
    	
        	return apiResponse;
        }
        
    	apiResponse = new ApiResponse(environment.getProperty("user.invalidDetails"), 2, null);
	
    	return apiResponse;
    }

    @Override
    public ApiResponse changePassword(String token, PasswordDto passwordDto) {
        try {
            String email = jwtToken.decodeToken(token);
            String encPass;
            User registeredUser = userRepository.findByEmail(email).orElseThrow(()-> new UserException());
            if (registeredUser != null) {
                if ((passwordDto.getNewPassword()).matches((passwordDto.getConfirmPassword()))) {
                    encPass = encoder.encodePassword(passwordDto.getNewPassword());
                    registeredUser.setPassword(encPass);
                    userRepository.save(registeredUser);
                	apiResponse = new ApiResponse(environment.getProperty("user.passwordChanged"), 1, null);
            	
                	return apiResponse;
                } else {
                	apiResponse = new ApiResponse(environment.getProperty("differentPassword"), 2, null);
            	
                	return apiResponse;
                }
            }
        } catch (Exception ignored) {
        }
        
    	apiResponse = new ApiResponse(environment.getProperty("user.invalidToken"), 3, null);
	
    	return apiResponse;
    }

    @Override
    public String verifyUser(String token) {
        String email = jwtToken.decodeToken(token);
        User registeredUser = userRepository.findByEmail(email).orElseThrow(()-> new UserException());
        if (registeredUser != null) {
            registeredUser.setIsVerified(true);
            userRepository.save(registeredUser);
            return environment.getProperty("user.verified");
        }
        return environment.getProperty("error");
    }

	@Override
	public ApiResponse getAllUsers() {
		apiResponse = new ApiResponse("User List", 1, userRepository.findAll());
		return apiResponse;
	}
}