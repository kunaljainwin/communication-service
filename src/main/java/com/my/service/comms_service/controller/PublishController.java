package com.my.service.comms_service.controller;

import com.my.service.comms_service.contracts.comms.request.EmailMessageContext;
import com.my.service.comms_service.contracts.comms.request.NotificationMessageContext;
import com.my.service.comms_service.contracts.comms.request.SmsMessageContext;
import com.my.service.comms_service.service.PublishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PublishController {

    @Autowired
    PublishService publishService;

    @PostMapping("/email")
    public ResponseEntity<?> publishEmail(@Valid @RequestBody EmailMessageContext message) {

        List<EmailMessageContext> messageList = new ArrayList<>();
        messageList.add(message);

        publishService.publishEmail(messageList);
        return ResponseEntity.ok("Email Published Successfully");
    }

    @PostMapping("/notification")
    public ResponseEntity<?> publishNotification(@Valid @RequestBody NotificationMessageContext message) {

        // Wrap single message into a list
        List<NotificationMessageContext> messageList = new ArrayList<>();
        messageList.add(message);

        // Call service and get result
        publishService.publishNotification(messageList);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/mock/action")
    public ResponseEntity<String> handleWebhookUrl(@RequestBody(required = false) String body) {
        System.out.println("Webhook received body: " + body); // or use log.info(...)
        return ResponseEntity.ok("Push Notification Published Successfully");
    }

    @PostMapping("/sms")
    public ResponseEntity<?> publishSMS(@Valid @RequestBody SmsMessageContext message) {

        List<SmsMessageContext> messageList = new ArrayList<>();
        messageList.add(message);

        publishService.publishSMS(messageList);
        return ResponseEntity.ok("SMS Published Successfully");
    }
}
