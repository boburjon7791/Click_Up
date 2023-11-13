package com.example.demo.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaMailSenderService {
    private final JavaMailSender javaMailSender;

    @Async
    @SneakyThrows
    public void send(String email, String message){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setText(message);
        InternetAddress address = new InternetAddress(email);
        mimeMessage.addRecipient(Message.RecipientType.TO,address);
        javaMailSender.send(mimeMessage);
        log.info("send message to {}",email);
    }
}
