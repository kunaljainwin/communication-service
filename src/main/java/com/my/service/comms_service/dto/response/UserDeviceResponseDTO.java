package com.my.service.comms_service.dto.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.util.enums.DeviceType;

/**
 * Response DTO for UserDevice.
 */
@Data
public class UserDeviceResponseDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("device_token")
    private String deviceToken;

    @JsonProperty("device_type")
    private DeviceType deviceType;   // ANDROID / IOS / WEB as String

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("app_version")
    private String appVersion;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
