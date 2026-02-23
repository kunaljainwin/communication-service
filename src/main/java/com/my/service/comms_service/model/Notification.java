package com.my.service.comms_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;

import com.my.service.comms_service.contracts.comms.enums.NotificationType;
import com.my.service.comms_service.util.enums.NotificationStatus;
/**
 * Notification Model
 * Matches DBML:
 *  - id (PK)
 *  - user_id (FK)
 *  - type (enum)
 *  - title, body, icon
 *  - timestamp (display)
 *  - deeplink
 *  - metadata (JSON)
 *  - action_mapping (JSON)
 *  - status (enum)
 *  - acted_at
 *  - created_at
 */
@Data
@Entity
@Table(
    name = "notification",
    indexes = {
        @Index(name = "idx_notification_user", columnList = "user_id"),
        @Index(name = "idx_notification_status", columnList = "status")
    }
)
public class Notification  {

    /** Primary Key */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(columnDefinition = "uuid")
    private UUID id;

    /** Recipient of the notification */
    @NotNull
    @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
    private UUID userId;

    /** Notification Type: PERSISTENT / GENERIC / CUSTOM */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    /** Title text */
    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String title;

    /** Body text */
    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    private String body;

    /** Optional icon */
    @Column(length = 255)
    private String icon;

    /** Optional deep link */
    @Column(length = 500)
    private String deeplink;

    /** JSON metadata */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private JsonNode metadata;

    /** JSON action mapping {approveApi, rejectApi} */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "action_mapping", columnDefinition = "jsonb")
    private JsonNode actionMapping;

    /** Status: pending / read / acted */
    @NotNull
    @Enumerated(EnumType.STRING)    
    @Column(name = "status", nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.CREATED;


    /** Time of approval or rejection */
    @Column(name = "acted_at")
    private Instant actedAt;

    @Column(name = "is_push_enabled")    
    private Boolean isPushEnabled;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "account_context", columnDefinition = "jsonb")
    private JsonNode accountContext;

}
