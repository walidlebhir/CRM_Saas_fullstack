package com.oualid.CRMSaas.Services;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private  final JavaMailSender javaemailsender ;

    public  EmailService(JavaMailSender javaMailSender){
        this.javaemailsender = javaMailSender ;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaemailsender.send(message);

        System.out.println("Email sent to: " + to);
    }
}
