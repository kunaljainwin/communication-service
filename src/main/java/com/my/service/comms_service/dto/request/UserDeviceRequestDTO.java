package com.my.service.comms_service.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.util.enums.DeviceType;

/**
 * Request DTO for creating/updating UserDevice
 */
@Data
public class UserDeviceRequestDTO {

    /** user_id → required */
    @NotNull(message = "user_id is required")
    @JsonProperty("user_id")
    private UUID userId;

    /** device_token → non-empty */
    @NotBlank(message = "device_token cannot be empty")
    @JsonProperty("device_token")
    private String deviceToken;

    /** device_type → must be one of ANDROID / IOS / WEB */
    @NotNull(message = "device_type is required")
    @Enumerated(EnumType.STRING)
    @JsonProperty("device_type")
    private DeviceType deviceType;

    /** active flag */
    @JsonProperty("active")
    private Boolean active = true;

    /** optional app version */
    @JsonProperty("app_version")
    private String appVersion;
}