package com.devfam.price.check.crawler.domain.service;

import com.devfam.price.check.crawler.PriceCheckCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceCheckCrawler.class);

    public void send(String toEmail, String subject, String body) {

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);

            mailSender.send(mailMessage);
            System.out.println("Mail sent successfully! to " + toEmail);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.out.println("Failed to send email. Error: " + e.getMessage());
        }
    }
}
