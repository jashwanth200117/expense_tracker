package com.expense.tracker.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
