package com.carrental.smartcar.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendRegistrationEmail(String toEmail, String username, String verificationLink) {
        String htmlMessage = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f7fa; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='color: #333333; text-align: center;'>Welcome to Our Smart Car Rental System!</h2>" +
                "<p style='font-size: 16px; color: #555555;'>Hi " + username + ",</p>" +
                "<p style='font-size: 16px; color: #555555;'>Thank you for signing up with us. We are excited to have you on board! To get started and verify your account, please click the button below.</p>" +
                "<div style='text-align: center; margin-top: 30px;'>" +
                "<a href='" + verificationLink + "' style='background-color: #1e90ff; color: #ffffff; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-size: 18px; font-weight: bold;'>" +
                "Verify Account" +
                "</a>" +
                "</div>" +
                "<p style='font-size: 14px; color: #888888; text-align: center; margin-top: 20px;'>If you did not sign up for this service, please ignore this email.</p>" +
                "<p style='font-size: 14px; color: #888888; text-align: center;'>Best regards,<br>Smart Car Rental Team</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration Successful - Verify Your Account");
        message.setText(htmlMessage);

        // Send email in HTML format
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(toEmail);
            messageHelper.setSubject("Registration Successful - Verify Your Account");
            messageHelper.setText(htmlMessage, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String toEmail, String username, String resetLink) throws MessagingException {
        String htmlMessage = "<html><body style='font-family: Arial; padding: 20px;'>"
                + "<h3>Hello " + username + ",</h3>"
                + "<p>You requested to reset your password. Click the link below to reset it:</p>"
                + "<a href='" + resetLink + "' style='padding: 10px 20px; background: #007bff; color: white; text-decoration: none;'>Reset Password</a>"
                + "<p>If you didn't request this, you can ignore this email.</p>"
                + "</body></html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject("Reset Your Password");
        helper.setText(htmlMessage, true);

        javaMailSender.send(message);
    }


}