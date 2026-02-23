package com.my.service.comms_service.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.contracts.comms.enums.ActionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionRequest {

    @NotNull(message = "Action type cannot be null" )
    @Enumerated(EnumType.STRING)
    @JsonProperty("action_type")
    private ActionType actionType;
}
