package com.my.service.comms_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.contracts.comms.enums.ActionType;
import com.my.service.comms_service.util.enums.ResultType;

@Data
public class NotificationActionRequestDTO {

    @NotNull(message = "notification_id is required")
    @JsonProperty("notification_id")
    private UUID notificationId;

    @NotNull(message = "user_id is required")
    @JsonProperty("user_id")
    private UUID userId;

    @NotNull(message = "action is required")
    @JsonProperty("action")
    private ActionType action;

    @NotBlank(message = "api_called is required")
    @JsonProperty("api_called")
    private String apiCalled;

    @NotNull(message = "result is required")
    @JsonProperty("result")
    private ResultType result;
}