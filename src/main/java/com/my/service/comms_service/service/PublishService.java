package com.my.service.comms_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.my.service.comms_service.contracts.comms.enums.NotificationType;
import com.my.service.comms_service.contracts.comms.event.NotificationDispatchEvent;
import com.my.service.comms_service.contracts.comms.request.EmailMessageContext;
import com.my.service.comms_service.contracts.comms.request.NotificationMessageContext;
import com.my.service.comms_service.contracts.comms.request.SmsMessageContext;

import lombok.extern.slf4j.Slf4j;

import com.my.service.comms_service.mapper.NotificationMapper;
import com.my.service.comms_service.model.Notification;
import com.my.service.comms_service.model.UserDevice;
import com.my.service.comms_service.util.enums.NotificationStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.service.comms_service.config.CommsConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for publishing communication messages to appropriate
 * queues
 * based on the message type (email, SMS, notification).
 * 
 * This service handles the routing of messages to the correct RabbitMQ exchange
 * with the appropriate routing key and tenant context.
 */

@Slf4j
@Service
public class PublishService {


    @Autowired
    CommsConfig commsConfig;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDeviceService userDeviceService;

    /**
     * Publishes email messages to the email queue.
     * 
     * @param data List of communication messages to be sent as emails
     * @throws RuntimeException if there's an error publishing to the queue
     */

    public void publishEmail(List<EmailMessageContext> data) {

        Map<String, Object> headers = new HashMap<>();

        // messagingAdapter.publishMessageToExchange(data, commsConfig.getExchange().getName(),
        //         commsConfig.getExchange().getType(), commsConfig.getKey().getEmail(), headers);
    }

    /**
     * Publishes SMS messages to the SMS queue.
     * 
     * @param data List of communication messages to be sent as SMS
     * @throws RuntimeException if there's an error publishing to the queue
     */

    public void publishSMS(List<SmsMessageContext> data) {

        Map<String, Object> headers = new HashMap<>();
        // messagingAdapter.publishMessageToExchange(data, commsConfig.getExchange().getName(),
        //         commsConfig.getExchange().getType(), commsConfig.getKey().getSms(), headers);
    }

    /**
     * Publishes notification messages to the notification queue.
     * 
     * @param data List of communication messages to be sent as notifications
     * @throws RuntimeException if there's an error publishing to the queue
     */
    public void publishNotification(List<NotificationMessageContext> data) {

        List<NotificationDispatchEvent> dispatchEvents = new ArrayList<>();

        try {
            // Process MessageContexts: create notifications and corresponding dispatch
            // events
            dispatchEvents = processAndCreateDispatchEvents(data);

        } catch (Exception e) {
            log.error("Error creating notifications or dispatch events: {}", e.getMessage());
            // failed.add(e.getMessage());
        }

        // Publish to queue only if we have any dispatch events
        if (!dispatchEvents.isEmpty()) {
            Map<String, Object> headers = new HashMap<>();
            // messagingAdapter.publishMessageToExchange(
            //         dispatchEvents,
            //         commsConfig.getExchange().getName(),
            //         commsConfig.getExchange().getType(),
            //         commsConfig.getKey().getNotification(),
            //         headers);
        }

    }
    // Main method
private List<NotificationDispatchEvent> processAndCreateDispatchEvents(
        List<NotificationMessageContext> messages) {

    List<NotificationDispatchEvent> dispatchEvents = new ArrayList<>();

    for (NotificationMessageContext messageContext : messages) {

        // STEP 1: Validate recipients
        Map<UUID, String> validUserToFcmToken = validateRecipients(messageContext);

        // STEP 2: Persist notifications if required
        Map<UUID, UUID> persistedNotifications = persistNotifications(messageContext, validUserToFcmToken);

        // STEP 3: Create dispatch event if push is enabled
        if (messageContext.getIsPushEnabled()) {
            NotificationDispatchEvent dispatchEvent = createDispatchEvent(
                    messageContext, validUserToFcmToken, persistedNotifications);
            dispatchEvents.add(dispatchEvent);
        }
    }

    return dispatchEvents;
}
private Map<UUID, String> validateRecipients(NotificationMessageContext messageContext) {
    Map<UUID, String> validUserToFcmToken = new HashMap<>();
    List<String> invalidRecipients = new ArrayList<>();

    for (String recipient : messageContext.getRecipients()) {
        UUID userId;

        // UUID validation
        try {
            userId = UUID.fromString(recipient);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid UUID for recipient: {}", recipient);
            invalidRecipients.add(recipient);
            continue;
        }

        if (messageContext.getIsPushEnabled()) {
            Optional<UserDevice> optDevice = userDeviceService.findLastUpdatedByUser(userId);
            if (optDevice.isEmpty() || optDevice.get().getDeviceToken() == null
                    || optDevice.get().getDeviceToken().isBlank()) {
                log.warn("No valid device/token for user {}", userId);
                invalidRecipients.add(recipient);
                continue;
            }
            validUserToFcmToken.put(userId, optDevice.get().getDeviceToken());
        } else {
            // Non-push, still valid for persistence
            validUserToFcmToken.put(userId, null);
        }
    }

    if (!invalidRecipients.isEmpty()) {
        log.warn("Partial recipients for {}: invalid={}",
                messageContext.getNotificationType(), invalidRecipients);
    }

    return validUserToFcmToken;
}
private Map<UUID, UUID> persistNotifications(
        NotificationMessageContext messageContext,
        Map<UUID, String> validUserToFcmToken) {

    Map<UUID, UUID> persistedNotifications = new HashMap<>();
    
    for (UUID userId : validUserToFcmToken.keySet()) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(messageContext.getSubject());
        notification.setBody(messageContext.getBody());
        notification.setIcon(messageContext.getIcon());
        notification.setDeeplink(messageContext.getDeeplink());
        if (messageContext.getMetaConfig() != null) {
            notification.setMetadata(objectMapper.valueToTree(messageContext.getMetaConfig()));
        }
        if (messageContext.getNotificationType() == NotificationType.CUSTOM) {
            notification.setActionMapping(
                    notificationMapper.convertActionsToMap(messageContext.getActions()));
        }
        notification.setType(messageContext.getNotificationType());
        notification.setStatus(NotificationStatus.CREATED);

        notification.setAccountContext(messageContext.getAccountContext());
        notification.setIsPushEnabled(messageContext.getIsPushEnabled());
        try {
            Notification saved = notificationService.create(notification);
            if (saved != null && saved.getId() != null) {
                persistedNotifications.put(userId, saved.getId());
            } else {
                log.error("Notification saved but ID null for user {}", userId);
            }
        } catch (Exception e) {
            log.error("Failed to save notification for user {}: ", userId, e);
        }
    }

    return persistedNotifications;
}
private NotificationDispatchEvent createDispatchEvent(
        NotificationMessageContext messageContext,
        Map<UUID, String> validUserToFcmToken,
        Map<UUID, UUID> persistedNotifications) {

    NotificationDispatchEvent dispatchEvent = new NotificationDispatchEvent();

    dispatchEvent.setConfigContext(messageContext.getConfigContext());
    dispatchEvent.setMetaConfig(messageContext.getMetaConfig());
    dispatchEvent.setNotificationType(messageContext.getNotificationType());
    dispatchEvent.setWebhookUrl(messageContext.getWebhookUrl());

    //TODO:
    // dispatchEvent.setActions(messageContext.g);
    // if(dispatchEvent.getNotificationType() == NotificationType.GENERIC){
        dispatchEvent.setDeeplink(messageContext.getDeeplink());
        dispatchEvent.setIcon(messageContext.getIcon());
        if (messageContext.getNotificationType() == NotificationType.CUSTOM) {
            dispatchEvent.setActions(messageContext.getActions());
        }
    // }

    // if(messageContext.getNotificationType() == NotificationType.GENERIC)
    // {
        dispatchEvent.setSubject(messageContext.getSubject());
        dispatchEvent.setBody(messageContext.getBody());
    // }
    // Recipients (userIds)
    List<String> validRecipients = validUserToFcmToken.keySet().stream()
            .filter(Objects::nonNull)
            .map(UUID::toString)
            .toList();

    // Device / FCM tokens
    List<String> deviceTokens = validUserToFcmToken.values().stream()
            .filter(Objects::nonNull)
            .toList();

    List<UUID> notificationIds = persistedNotifications.values().stream().toList();

    dispatchEvent.setRecipients(validRecipients);
    dispatchEvent.setDevice_tokens(deviceTokens);
    if(messageContext.getNotificationType() == NotificationType.CUSTOM){
        dispatchEvent.setNotification_ids(notificationIds);
    }
    dispatchEvent.setAccountContext(messageContext.getAccountContext());
    dispatchEvent.setIsPushEnabled(messageContext.getIsPushEnabled());
    return dispatchEvent;
}

    // private List<NotificationDispatchEvent> processAndCreateDispatchEvents1(
    //         List<NotificationMessageContext> data) {

    //     List<NotificationDispatchEvent> dispatchEvents = new ArrayList<>();

    //     for (NotificationMessageContext messageContext : data) {

    //         List<UUID> notificationIds = new ArrayList<>();
    //         List<UUID> validRecipients = new ArrayList<>();
    //         List<String> fcmTokens = new ArrayList<>();
    //         List<String> invalidRecipients = new ArrayList<>();

    //         // STEP 1: Validate & collect FCM tokens
    //         for (String recipient : messageContext.getRecipients()) {

    //             UUID userId;

    //             // UUID validation
    //             try {
    //                 userId = UUID.fromString(recipient);
    //             } catch (IllegalArgumentException ex) {
    //                 log.error("Invalid UUID for recipient: {}", recipient);
    //                 invalidRecipients.add(recipient);
    //                 continue;
    //             }

    //             if (messageContext.getNotificationType().isPushEnabled()) {

    //                 Optional<UserDevice> optDevice = userDeviceService.findLastUpdatedByUser(userId);

    //                 if (optDevice.isEmpty()) {
    //                     log.warn("No device for user {}", userId);
    //                     invalidRecipients.add(recipient);
    //                     continue;
    //                 }

    //                 String token = optDevice.get().getDeviceToken();

    //                 if (token == null || token.isBlank()) {
    //                     log.warn("Empty FCM token for user {}", userId);
    //                     invalidRecipients.add(recipient);
    //                     continue;
    //                 }

    //                 fcmTokens.add(token);
    //             }
    //             validRecipients.add(userId);
    //         }
    //         // Log partial valid recipients (non-fatal)
    //         if (validRecipients.size() != messageContext.getRecipients().size()) {
    //             log.warn(
    //                     "Partial recipients: valid={}, total={}, invalid={}",
    //                     validRecipients.size(),
    //                     messageContext.getRecipients().size(),
    //                     invalidRecipients);
    //         }

    //         // STEP 2: Create DB notifications for ONLY valid recipients
    //         if (messageContext.getNotificationType().isPersisted()) {
    //             for (UUID userId : validRecipients) {

    //                 Notification notification = new Notification();
    //                 notification.setUserId(userId);
    //                 notification.setTitle(messageContext.getSubject());
    //                 notification.setBody(messageContext.getBody());
    //                 notification.setIcon(messageContext.getIcon());
    //                 notification.setDeeplink(messageContext.getDeeplink());

    //                 JsonNode metaNode = objectMapper.valueToTree(messageContext.getMetaConfig());
    //                 notification.setMetadata(metaNode);

    //                 notification.setActionMapping(
    //                         notificationMapper.convertActionsToMap(messageContext.getActions()));

    //                 notification.setType(messageContext.getNotificationType());
    //                 notification.setStatus(NotificationStatus.CREATED);

    //                 Notification saved;

    //                 try {
    //                     saved = notificationService.create(notification);
    //                 } catch (Exception e) {
    //                     log.error("Failed to save notification for user {}: {}", userId, e.getMessage());
    //                     continue;
    //                 }

    //                 if (saved == null || saved.getId() == null) {
    //                     log.error("Notification saved but ID null for user {}", userId);
    //                     continue;
    //                 }

    //                 notificationIds.add(saved.getId());
    //             }

    //             // Final consistency check
    //             if (notificationIds.size() != fcmTokens.size()) {
    //                 log.error(
    //                         "Mismatch: notifications={} tokens={}",
    //                         notificationIds.size(),
    //                         fcmTokens.size());
    //                 continue; // skip this event
    //             }
    //         }

    //         // STEP 3: Create dispatch event
    //         if (messageContext.getNotificationType().isPushEnabled()) {
    //             NotificationDispatchEvent dispatchEvent = new NotificationDispatchEvent();
    //             dispatchEvent.setConfigContext(messageContext.getConfigContext());
    //             dispatchEvent.setSubject(messageContext.getSubject());
    //             dispatchEvent.setBody(messageContext.getBody());
    //             dispatchEvent.setMetaConfig(messageContext.getMetaConfig());
    //             dispatchEvent.setNotificationType(messageContext.getNotificationType());
    //             dispatchEvent.setRecipients(fcmTokens);// TODO : in case of not pushed. notification idsfcmTokens will
    //                                                    // be empty.
    //             dispatchEvent.setWebhookUrl(messageContext.getWebhookUrl());
    //             dispatchEvent.setNotification_ids(notificationIds); // TODO : in case of not persisted. notification ids
    //                                                                 // will be empty.

    //             dispatchEvents.add(dispatchEvent);
    //         }
    //     }

    //     return dispatchEvents;
    // }

}
