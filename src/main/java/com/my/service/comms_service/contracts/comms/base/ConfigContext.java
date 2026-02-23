package com.my.service.comms_service.contracts.comms.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.service.comms_service.contracts.comms.enums.SenderType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigContext {

    // BRAND, SYSTEM, USER
    @NotNull(message = "sender_type is required")
    @JsonProperty("sender_type")
    private SenderType senderType;

    @NotNull(message = "tenant_id is required")
    @JsonProperty("tenant_id")
    private UUID tenantId;

    // Optional for USER
    @JsonProperty("user_id")
    private UUID userId;
    
    @Email(message = "from_address must be a valid email")
    @JsonProperty("from_address")
    private String fromAddress; // Optional exact override like "custom@brand.com"
    
    @NotBlank(message = "from_name is required")
    @Pattern(
        regexp = "^[a-zA-Z '-]*$",
        message = "from_name can only contain letters, spaces, apostrophes, or hyphens"
    )
    @JsonProperty("from_name")
    private String fromName;

    // Conditional validation
    @AssertTrue(message = "tenant_id is required when sender_type is BRAND")
    private boolean isTenantValid() {
        return senderType != SenderType.BRAND || tenantId != null;
    }

    @AssertTrue(message = "user_id is required when sender_type is USER")
    private boolean isUserValid() {
        return senderType != SenderType.USER || userId != null;
    }
}