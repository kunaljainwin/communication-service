package com.my.service.comms_service.contracts.comms.enums;

public enum SenderType {
    BRAND,   // Use TenantCommsConfig
    SYSTEM,  // Use system-level defaults
    USER     // Use UserCommsConfig
}

