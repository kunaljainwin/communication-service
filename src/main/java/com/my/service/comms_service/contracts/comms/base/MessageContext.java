package com.my.service.comms_service.contracts.comms.base;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.contracts.comms.annotation.ValidRecipients;
import com.my.service.comms_service.contracts.comms.enums.ChannelType;

@Data
@ValidRecipients
public abstract class MessageContext {

    @Valid
    @NotNull(message = "config_context is required")
    @JsonProperty("config_context")
    private ConfigContext configContext;

    @NotEmpty(message = "recipients list cannot be empty")
    @Size(max = 50, message = "Maximum 50 recipients allowed")
    private List<@NotBlank(message = "recipient cannot be blank") String> recipients;


    @NotBlank(message = "subject is required")
    @Size(max = 200, message = "subject cannot exceed 200 characters")
    @JsonProperty("subject")
    private String subject;   // Language Resolved Subject String
    
    @NotBlank(message = "body is required")
    @JsonProperty("body")
    private String body;      // Language Resolved Body String

    // Webhook URL for success/error notifications
    // @NotNull(message = "webhook_url must contain a valid URL")
   @Pattern(
           regexp = "^(https?://)[\\w.-]+(:\\d+)?(/.*)?$",
           message = "webhook_url must be a valid HTTP/HTTPS URL"
   )
    @JsonProperty("webhook_url")
    private String webhookUrl;
    
    @JsonProperty("meta")
    private Object metaConfig;

    /**
     * Returns the channel type for this message.
     * Must be implemented by subclasses.
     */
    @JsonIgnore
    public abstract ChannelType getChannel();
}
