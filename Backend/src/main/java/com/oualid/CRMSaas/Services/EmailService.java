package com.oualid.CRMSaas.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            // true = HTML activé
            helper.setText(body, true);

            javaMailSender.send(message);

            System.out.println("Email sent to: " + to);

        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi email: " + e.getMessage());
        }
    }
}