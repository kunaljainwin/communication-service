package com.my.service.comms_service.contracts.comms.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.contracts.comms.enums.ActionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Action {

    @NotNull(message = "type is required")
    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private ActionType type;

    @NotNull(message = "uri must not be null")
    @Pattern(
            regexp = "^(https?://)[\\w.-]+(:\\d+)?(/.*)?$",
            message = "uri must be a valid HTTP/HTTPS URL"
    )
    @JsonProperty("uri")
    private String uri;
}
