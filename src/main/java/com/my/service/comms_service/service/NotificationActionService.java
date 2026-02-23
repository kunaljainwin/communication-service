package com.my.service.comms_service.service;

import com.my.service.comms_service.model.NotificationAction;
import com.my.service.comms_service.repository.INotificationActionRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotificationActionService {

    @Autowired
    private INotificationActionRepository notificationActionRepository;

    public NotificationAction create(NotificationAction notificationAction) {
        return notificationActionRepository.save(notificationAction);
    }

    public NotificationAction update(UUID id, NotificationAction update) {
        NotificationAction existing = notificationActionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NotificationAction not found with id: " + id));

        // --- Apply partial updates only if non-null (PATCH behavior) ---

        // if (update.getNotificationId() != null) {
        // existing.setNotificationId(update.getNotificationId());
        // }

        // if (update.getUserId() != null) {
        // existing.setUserId(update.getUserId());
        // }

        // if (update.getAction() != null) {
        // existing.setAction(update.getAction()); // enum APPROVE / REJECT
        // }

        // if (update.getApiCalled() != null) {
        // existing.setApiCalled(update.getApiCalled());
        // }

        // Both must be provided together or not at all
        boolean incomingHasBoth = update.getResult() != null && update.getActedAt() != null;
        boolean incomingHasPartial = (update.getResult() != null && update.getActedAt() == null) ||
                (update.getResult() == null && update.getActedAt() != null);

        // Reject partial attempts
        if (incomingHasPartial) {
            throw new IllegalStateException("result and actedAt must be updated together.");
        }

        // If already set once, no further updates allowed
        boolean alreadySet = existing.getResult() != null || existing.getActedAt() != null;
        if (alreadySet && incomingHasBoth) {
            throw new IllegalStateException("result and actedAt can only be set once.");
        }

        // If not yet set and incoming provides both => allow update
        if (!alreadySet && incomingHasBoth) {
            existing.setResult(update.getResult());
            existing.setActedAt(update.getActedAt());
        }

        // @PreUpdate will auto-update updatedAt if present in entity
        return notificationActionRepository.save(existing);
    }

    public NotificationAction replace(UUID id, NotificationAction notificationAction) {
        UUID uuid = id;
        if (!notificationActionRepository.existsById(uuid)) {
            throw new EntityNotFoundException("NotificationAction not found with id: " + id);
        }
        notificationAction.setId(uuid);
        return notificationActionRepository.save(notificationAction);
    }

    public void delete(UUID id) {
        notificationActionRepository.deleteById(id);
    }

    public Optional<NotificationAction> findById(UUID id) {
        return notificationActionRepository.findById(id);
    }

}
