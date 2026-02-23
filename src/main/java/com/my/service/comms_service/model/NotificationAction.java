package com.my.service.comms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.UUID;

import com.my.service.comms_service.contracts.comms.enums.ActionType;
import com.my.service.comms_service.util.enums.ResultType;

/**
 * Model for NotificationAction
 *
 * Matches DBML:
 *  - id PK
 *  - notification_id FK
 *  - user_id FK
 *  - action (APPROVE / REJECT)
 *  - api_called
 *  - result (SUCCESS / FAILURE)
 *  - acted_at auto timestamp
 */
@Data
@Entity
@Table(name = "notification_actions")
public class NotificationAction  {

    /** Primary Key */
    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Foreign key → notifications.id */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "notification_id",
            nullable = false,
            columnDefinition = "uuid"
    )
    private Notification notification;
    
    @JsonIgnore
    public UUID getNotificationId() {
        return notification != null ? notification.getId() : null;
    }

    /** User who performed the action */
    @NotNull
    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    private UUID userId;

    /** APPROVE / REJECT */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private ActionType action;

    /** Internal API endpoint executed */
    @NotBlank
    @Column(name = "api_called", columnDefinition = "text", nullable = false)
    private String apiCalled;

    /** SUCCESS / FAILURE */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 20)
    private ResultType result;

    /** Timestamp of the action */
    @Column(name = "acted_at", nullable = false)
    private Instant actedAt = Instant.now();
}
