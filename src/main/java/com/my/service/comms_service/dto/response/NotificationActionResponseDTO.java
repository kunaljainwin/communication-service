package com.my.service.comms_service.dto.response;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.contracts.comms.enums.ActionType;
import com.my.service.comms_service.util.enums.ResultType;

@Data
public class NotificationActionResponseDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("notification_id")
    private UUID notificationId;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("action")
    private ActionType action;

    @JsonProperty("api_called")
    private String apiCalled;

    @JsonProperty("result")
    private ResultType result;

    @JsonProperty("acted_at")
    private Instant actedAt;
}