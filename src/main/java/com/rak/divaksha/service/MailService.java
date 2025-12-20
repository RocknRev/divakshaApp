package com.rak.divaksha.service;

public interface MailService {

    public void sendMail(String to, String subject, String body);
    public void sendOrderConfirmation(String to, Long orderId, String amount);
    public void sendOrderPaidMail(String to, Long orderId);
    public void sendOrderRejectedMail(String to, Long orderId);
    public void sendOtp(String to, String otp);
}
