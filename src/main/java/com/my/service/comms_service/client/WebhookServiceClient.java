package com.my.service.comms_service.client;
// package com.my.service.comms_service.client;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.*;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestClientException;
// import org.springframework.web.client.RestTemplate;

// import com.my.service.comms_service.contracts.base.MessageContext;

// import lombok.extern.slf4j.Slf4j;

// import java.net.URI;
// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.UUID;

// @Slf4j
// @Service
// public class WebhookServiceClient {


//     @Autowired
//     private RestTemplate restTemplate;

//     /**
//      * Sends error notification to client webhook URL
//      */
//     public void sendErrorNotification(MessageContext message, Exception error) {
//         if (message.getWebhookUrl() == null || message.getWebhookUrl().toString().isBlank()) {
//             log.warn("provided wrong Webhook URL :",message.getWebhookUrl()," missed this error :",error.getMessage());
//             return;
//         }

//         try {
//             Map<String, Object> payload = new HashMap<>();
//             payload.put("status", "FAILED");
//             payload.put("channel", message.getChannel().toString());
//             payload.put("errorMessage", error.getMessage());
//             payload.put("timestamp", LocalDateTime.now().toString());
//             payload.put("metaConfig", message.getMetaConfig());


//             sendWebhookRequest(message.getWebhookUrl(), payload ,  message.getConfigContext().getTenantId());

//         } catch (Exception e) {
//             System.out.println("Failed to send error notification via webhook: " + e.getMessage());
//         }
//     }

//     /**
//      * Sends success notification to client webhook URL
//      */
//     public void sendSuccessNotification(MessageContext message) {
//         if (message.getWebhookUrl() == null || message.getWebhookUrl().toString().isBlank()) {
//             log.warn("provided wrong Webhook URL :",message.getWebhookUrl()," missed a message of type:",message.getChannel());
//             return;
//         }

//         try {
//             Map<String, Object> payload = new HashMap<>();
//             payload.put("status", "SUCCESS");
//             payload.put("channel", message.getChannel().toString());
//             payload.put("message", "Email sent successfully");
//             payload.put("timestamp", LocalDateTime.now().toString());
//             payload.put("metaConfig", message.getMetaConfig());

//             sendWebhookRequest(message.getWebhookUrl(), payload, message.getConfigContext().getTenantId());

//         } catch (Exception e) {
//             System.out.println("Failed to send success notification via webhook: " + e.getMessage());
//         }
//     }

//     /**
//      * Makes HTTP POST request to webhook URL
//      */
//     private void sendWebhookRequest(URI webhookUrl, Map<String, Object> payload, UUID tenantId) {
//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);

//         HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

//         try {
//             ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);
//             log.info("Webhook sent. Status: {}", response.getStatusCode());
//         } catch (RestClientException e) {
//             log.error("Failed to send webhook to {}: {}", webhookUrl, e.getMessage());
//         }
//     }   

// }
