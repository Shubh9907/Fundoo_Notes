package com.bridgelabz.fundoo_notes.service;

import com.bridgelabz.fundoo_notes.utility.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * This Class is used for sending the mail to the user
 * @author Shubham Verma
 */
@Component
public class MailService {

    public boolean throughForget;

    @Autowired
    JwtToken jwtToken;

    @Autowired
    Environment environment;

    public String sendMail(String email) {
        final String host = "smtp.gmail.com";
        final String sender = "mailboxofshubh@gmail.com";
        final String pass = "Verma@123Shubham";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols","TLSv1.2");

        String token = jwtToken.generateToken(email);

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, pass);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new  InternetAddress(email));
            if (!throughForget) {
                message.setSubject("Fundoo Email verification link");
                message.setText("Click on the below link to verify your email id http://localhost:8080/verifyUser/" + token);
            }else {
                message.setSubject("Fundoo Password Reset Link");
                message.setText("Click on the link and use token given below to reset your password http://localhost:8080/swagger-ui.html#!/controller/changePasswordUsingPUT \n Your password reset Token is:- " + token);
            }
            Transport.send(message);
            throughForget = false;
            return environment.getProperty("emailSent");
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Some Error occurred";
        }
    }
}
