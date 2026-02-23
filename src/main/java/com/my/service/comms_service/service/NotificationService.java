package com.my.service.comms_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.my.service.comms_service.contracts.comms.enums.ActionType;
import com.my.service.comms_service.dto.request.ActionRequest;
import com.my.service.comms_service.model.Notification;
import com.my.service.comms_service.model.NotificationAction;
import com.my.service.comms_service.util.enums.NotificationStatus;
import com.my.service.comms_service.util.enums.ResultType;
import com.my.service.comms_service.repository.INotificationActionRepository;
import com.my.service.comms_service.repository.INotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private INotificationActionRepository notificationActionRepository;
    @Autowired
    private RestTemplate restTemplate;

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllByUserId(UUID userId){
        return notificationRepository.findAllByUserId(userId);
    }
    public Notification update(UUID id, Notification update) {
        Notification existing = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));

        // ------------------------------------------------
        // STATUS CAN ONLY MOVE FORWARD (one direction)
        // ------------------------------------------------
        if (update.getStatus() != null) {
            NotificationStatus current = existing.getStatus();
            NotificationStatus next = update.getStatus();

            if (!current.canTransitionTo(next)) {
                throw new IllegalArgumentException(
                        String.format("Invalid status transition: %s → %s", current, next));
            }

            existing.setStatus(next);

            // Optionally update actedAt when moving to ACTED
            if (next == NotificationStatus.ACTED) {
                existing.setActedAt(Instant.now());
            }
        }

        // updated_at auto-handled if using @PreUpdate
        return notificationRepository.save(existing);
    }

    public void delete(UUID id) {
        notificationRepository.deleteById(id);
    }

    public Optional<Notification> findById(UUID id) {
        return notificationRepository.findById(id);
    }

    @Transactional
    public void performAction(UUID id, ActionRequest actionRequest) {
        try {
            
            ActionType actionType = actionRequest.getActionType();
            Optional<Notification> optNotification = notificationRepository.findById(id);

            if (optNotification.isEmpty()) {
                throw new EntityNotFoundException("Notification not found for id: " + id);
            }

            Notification notification = optNotification.get();

            // 1. Prevent duplicate / repeated actions → idempotency
            if (!notification.getStatus().canTransitionTo(NotificationStatus.ACTED)) {
                // Already acted or invalid transition
                return; // Idempotent return
            }

            JsonNode actionMapping = notification.getActionMapping();

            if (actionMapping == null || !actionMapping.has(actionType.name())) {
                throw new IllegalStateException("No action mapping found for action: " + actionType);
            }

            // Extract URL (simple string)
            String apiUrl = actionMapping.get(actionType.name()).asText();

            if (apiUrl == null || apiUrl.isBlank()) {
                throw new IllegalStateException("Invalid API URL for action: " + actionType);
            }

            // 3. Make external API call
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Prepare request body
            HttpEntity<String> request = new HttpEntity<>(
                    notification.getMetadata() != null ?notification.getMetadata().toString():null,
                    headers);
                    
            NotificationAction actionEntry = new NotificationAction();
            actionEntry.setUserId(notification.getUserId());
            actionEntry.setAction(actionType);
            actionEntry.setApiCalled(apiUrl);
            actionEntry.setResult(ResultType.FAILURE);
            actionEntry.setActedAt(Instant.now());
                    
                    
            // Make API call based on method
            ResponseEntity<String> response = actionWebhook(apiUrl, request);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Webhook success: {}", response.getBody());
                actionEntry.setResult(ResultType.SUCCESS);

            } else {
                log.warn("Webhook failed: {}", response.getBody());
                actionEntry.setResult(ResultType.FAILURE);
            }
            actionEntry.setNotification(notification);
            notificationActionRepository.save(actionEntry);
            
            // Update notification status → ACTED
            notification.setStatus(NotificationStatus.ACTED);
            notification.setActedAt(Instant.now());
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    public ResponseEntity<String> actionWebhook(String apiUrl, HttpEntity<String> request) {
        try {
            return restTemplate.postForEntity(apiUrl, request, String.class);

        } catch (RestClientException ex) {
            log.error("External API failed: {}", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY) // 502: external service error
                    .body(ex.getMessage());
        }
    }
}
