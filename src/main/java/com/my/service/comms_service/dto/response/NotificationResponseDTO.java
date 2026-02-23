package com.my.service.comms_service.dto.response;

import lombok.Data;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.Instant;
import java.util.UUID;

import com.my.service.comms_service.contracts.comms.enums.NotificationType;
import com.my.service.comms_service.util.enums.NotificationStatus;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationResponseDTO {

        private UUID id;
        private UUID userId;
        private NotificationType type;
        private String title;
        private String body;
        private String icon;
        private String deeplink;
        // private Object metadata;
        private JsonNode accountContext;
        private JsonNode actionMapping;
        private NotificationStatus status;
        private Instant actedAt;
        private Instant createdAt;

}
