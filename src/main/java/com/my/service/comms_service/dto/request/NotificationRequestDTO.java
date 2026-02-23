package com.my.service.comms_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.my.service.comms_service.contracts.comms.enums.NotificationType;
import java.util.UUID;
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationRequestDTO {

    @NotNull(message = "user_id is required")
    private UUID userId;

    @NotNull(message = "type is required")
    private NotificationType type;

    @NotBlank(message = "title is required")
    private String title;
    private String body;
    private String icon;
    private String deeplink;
    private JsonNode metadata;
    private JsonNode actionMapping;
    private Boolean isPushEnabled;
    private JsonNode accountContext;
}