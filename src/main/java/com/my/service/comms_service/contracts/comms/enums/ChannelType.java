package com.my.service.comms_service.contracts.comms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelType {

    email("email"),
    sms("sms"),
    notification("notification");

    @JsonValue
    private final String val;

    @JsonCreator
    public static ChannelType fromVal(String val) {
        for (ChannelType op : values()) {
            if (op.val.equals(val)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown EventType: " + val);
    }
}
