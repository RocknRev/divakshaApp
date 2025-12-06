package com.rak.divaksha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.from}")
	private String mailFrom;

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(mailFrom);
        mailSender.send(message);
    }

    public void sendOrderConfirmation(String to, Long orderId, String productName, String amount) {
        String subject = "Order Confirmation - #" + orderId;
        String body =
                "Hello,\n\n" +
                "Your order has been successfully placed.\n\n" +
                "Order ID: " + orderId + "\n" +
                "Product: " + productName + "\n" +
                "Amount Paid: ₹" + amount + "\n\n" +
                "Thank you for shopping with us!\n" +
                "Team Divaksha";

        sendMail(to, subject, body);
    }

    public void sendOrderPaidMail(String to, Long orderId) {
        String subject = "Order Approved";
        String body =
                "Hello,\n\n" +
                "Your order #" + orderId + " payment has been verified and approved.\n\n" +
                "Thank you for your purchase!\n" +
                "Team Divaksha";

        sendMail(to, subject, body);
    }

    public void sendOrderRejectedMail(String to, Long orderId) {
        String subject = "Order Rejected";
        String body =
                "Hello,\n\n" +
                "We regret to inform you that your order #" + orderId + " could not be approved.\n\n" +
                "If a payment was made, it will be refunded shortly. You will receive a separate update once the refund is processed.\n\n" +
                "If you believe this is a mistake or need any help, please contact our support team.\n\n" +
                "Thank you,\n" +
                "Team Divaksha";

        sendMail(to, subject, body);
    }

    public void sendOtp(String to, String otp) {
        String subject = "Your Divaksha Email Verification OTP";
    
        String body =
                "Hello,\n\n" +
                "Your One-Time Password (OTP) for completing your registration at Divaksha is:\n\n" +
                otp + "\n\n" +
                "This OTP is valid for 3 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\n" +
                "Team Divaksha";
    
        sendMail(to, subject, body);
    }
    
}
