package com.my.service.comms_service.contracts.comms.event;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.my.service.comms_service.contracts.comms.request.NotificationMessageContext;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDispatchEvent extends NotificationMessageContext {
    private List<UUID> notification_ids; 
    private List<String> device_tokens;
}
