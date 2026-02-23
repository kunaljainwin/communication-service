package com.my.service.comms_service.contracts.comms.request;

import com.my.service.comms_service.contracts.comms.base.Action;
import com.my.service.comms_service.contracts.comms.base.MessageContext;
import com.my.service.comms_service.contracts.comms.enums.ChannelType;
import com.my.service.comms_service.contracts.comms.enums.NotificationType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationMessageContext extends MessageContext {

    @NotNull(message = "notification_type is required")
    private NotificationType notificationType;
    
    @NotNull(message = "is_push_enabled is required")
    private Boolean isPushEnabled;
    
    @NotNull(message = "account_context is required")
    private JsonNode accountContext;

    @NotNull(message = "icon is required")
    private String icon;

    @Valid
    List<Action> actions;

    @Pattern(
        regexp = "^(?:[a-zA-Z][a-zA-Z0-9+.-]*://).+$",
        message = "deeplink must be a valid URI with scheme"
    ) 
    private String deeplink;

    @Override
    public ChannelType getChannel() {
        return ChannelType.notification;
    }

}
