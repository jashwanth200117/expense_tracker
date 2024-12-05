package com.expense.tracker.controller;

import com.expense.tracker.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/api/test-email")
    public String sendTestEmail(@RequestParam String to) {
        String subject = "Test Email from Expense Tracker";
        String body = "This is a test email sent from your Spring Boot application.";
        emailService.sendEmail(to, subject, body);
        return "Test email sent to " + to;
    }
}
